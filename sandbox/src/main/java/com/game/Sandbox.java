package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.engine.dialogue.DialogueManager;
import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.logic.PlayerController;
import com.engine.inventory.components.Inventory;
import com.engine.ecs.components.physics.BoxCollider;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.inventory.models.ItemStack;
import com.engine.rendering.RenderSystem;
import com.engine.rendering.components.SpriteRenderer;
import com.engine.GameSystem;
import com.engine.gdx.rendering.GDXAssetManager;
import com.engine.gdx.rendering.GDXRender;
import com.engine.gdx.systems.GDXInputSystem;
import com.engine.gdx.systems.GDXMovementSystem;
import com.engine.rendering.logic.SpriteRegistry;
import com.engine.rendering.models.SpriteDefinition;
import com.engine.ecs.ECSSerializer;
import com.engine.math.Random;

import java.util.ArrayList;
import java.util.List;

// QUESTION FOR SELF: maybe use Game instead of ApplicationAdapter so I can use Screen and setScreen
public class Sandbox extends ApplicationAdapter {
  private boolean isPaused = false;

  private ShapeRenderer shapeRenderer;
  private OrthographicCamera camera;
  private Viewport viewport;

  private GDXAssetManager assetManager;
  private GDXRender renderContext;

  private ECSSerializer serializer;

  private GDXInputSystem inputSystem;
  private GDXMovementSystem movementSystem;
  private List<GameSystem> systems;

  private List<Entity> entities;
  private Entity player;

  private static final DialogueManager dialogueManager = new DialogueManager();

  @Override
  public void create() {
    shapeRenderer = new ShapeRenderer();
    entities = new ArrayList<>();

    systems = new ArrayList<>();

    camera = new OrthographicCamera();
    viewport = new FitViewport(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, camera);

    serializer = new ECSSerializer();

    assetManager = new GDXAssetManager();
    renderContext = new GDXRender(assetManager, camera);

    ItemRegistry.loadAllItems("/items/items.json");
    dialogueManager.loadAllGraphs("/graphs");
    assetManager.loadTexture("sprites/playerIdle.png");
    SpriteRegistry.register("playerIdle", new SpriteDefinition("sprites/playerIdle.png", 0, 0, 192, 192));

    // Should just load entities from the loadGame() so that I don't have to create
    // entities here (if 20+ its gonna get real messy)
    player = new Entity("Tav");
    player.addComponent(new Transform(400, 300));
    player.addComponent(new BoxCollider(10, 10));
    player.addComponent(new Inventory(10));
    player.getComponent(Inventory.class).addItem(new ItemStack("iron_sword", 1));

    player.addComponent(new PlayerController());
    player.addComponent(new SpriteRenderer("playerIdle", 192, 192, 0, -(96 /
        2)));

    entities.add(player);

    Entity npcBlacksmith = new Entity("[BLACKSMITH] Brokk Ironjaw", "blacksmith_start");
    npcBlacksmith.addComponent(new Transform(200, 100));
    entities.add(npcBlacksmith);

    // Game systems:
    // Have input system still stored normally because I want to access keybindings
    inputSystem = new GDXInputSystem(player.getComponent(PlayerController.class),
        player.getComponent(SpriteRenderer.class));
    movementSystem = new GDXMovementSystem(player.getComponent(PlayerController.class),
        player.getComponent(Transform.class));

    systems.add(inputSystem);
    systems.add(new RenderSystem(renderContext, entities, true));
    systems.add(movementSystem);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
    renderContext.resize(width, height);
  }

  @Override
  public void render() {
    // The looping function from libGDX
    float delta = Gdx.graphics.getDeltaTime();

    eventHandler();
    update(delta);
    renderGDX();
  }

  private void eventHandler() {
    for (GameSystem system : systems) {
      system.eventHandler();
    }
  }

  private void update(float delta) {
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.PAUSE))) {
      isPaused = !isPaused;
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.SAVE))) {
      saveGame();
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.LOAD))) {
      loadGame();
    }

    if (!isPaused) {
      for (GameSystem system : systems) {
        system.update(delta);
      }
    }
    camera.update();
  }

  private void renderGDX() {
    for (GameSystem system : systems) {
      system.render();
    }
  }

  private void saveGame() {
    String playerData = serializer.saveEntity(player);

    FileHandle file = Gdx.files.local("saves/player_save.json");
    file.writeString(playerData, false);

    System.out.println("Saved game");
  }

  private void loadGame() {
    FileHandle file = Gdx.files.local("saves/player_save.json");

    if (file.exists()) {
      String jsonText = file.readString();
      Entity loadedPlayer = serializer.loadEntity(jsonText);

      entities.remove(this.player); // Remove so I dont update old player data
      this.player = loadedPlayer;
      entities.add(this.player);

      // I don't like the solution below. Either I can do this
      // or I can defaultly give the inputSystem and movementSystem
      // all the entities so that when I update entities here
      // they get the new player, but that would mean that they
      // would have to process the whole entities list instead of just
      // one player. I don't know what to do with this one yet.
      inputSystem.setTarget(
          player.getComponent(PlayerController.class),
          player.getComponent(SpriteRenderer.class));

      movementSystem.setTarget(
          player.getComponent(PlayerController.class),
          player.getComponent(Transform.class));

      System.out.println("Game loaded");
    }
  }

  @Override
  public void dispose() {
    if (shapeRenderer != null) {
      shapeRenderer.dispose();
    }
    if (renderContext != null) {
      renderContext.dispose();
    }
    if (assetManager != null) {
      assetManager.dispose();
    }

    System.exit(0); // This fixed the Segmentation Fault due to being in WSL
  }
}
