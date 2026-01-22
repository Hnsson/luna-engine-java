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
import com.engine.inventory.components.Inventory;
import com.engine.ecs.components.physics.BoxCollider;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.inventory.models.ItemStack;
import com.engine.rendering.RenderSystem;
import com.engine.rendering.components.SpriteRenderer;
import com.engine.rendering.gdx.GDXAssetManager;
import com.engine.rendering.gdx.GDXRender;
import com.engine.rendering.logic.SpriteRegistry;
import com.engine.rendering.models.SpriteDefinition;
import com.engine.ecs.ECSSerializer;

import java.util.ArrayList;
import java.util.List;

// QUESTION FOR SELF: maybe use Game instead of ApplicationAdapter so I can use Screen and setScreen
public class Sandbox extends ApplicationAdapter {
  private ShapeRenderer shapeRenderer;
  private OrthographicCamera camera;
  private Viewport viewport;

  private GDXAssetManager assetManager;
  private GDXRender renderContext;
  private RenderSystem renderer;

  private ECSSerializer serializer;

  private List<Entity> entities;
  private Entity player;

  private static final DialogueManager dialogueManager = new DialogueManager();

  @Override
  public void create() {
    shapeRenderer = new ShapeRenderer();
    entities = new ArrayList<>();

    camera = new OrthographicCamera();
    viewport = new FitViewport(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, camera);

    serializer = new ECSSerializer();

    assetManager = new GDXAssetManager();
    renderContext = new GDXRender(assetManager, camera);
    renderer = new RenderSystem(renderContext);

    ItemRegistry.loadAllItems("/items/items.json");
    dialogueManager.loadAllGraphs("/graphs");
    assetManager.loadTexture("sprites/playerIdle.png");
    SpriteRegistry.register("playerIdle", new SpriteDefinition("sprites/playerIdle.png", 0, 0, 192, 192));

    player = new Entity("Tav");
    player.addComponent(new Transform(400, 300));
    player.addComponent(new BoxCollider(10, 10));
    player.addComponent(new Inventory(10));
    player.getComponent(Inventory.class).addItem(new ItemStack("iron_sword", 1));

    player.addComponent(new SpriteRenderer("playerIdle", 192, 192, 0, -(96 /
        2)));

    entities.add(player);

    Entity npcBlacksmith = new Entity("[BLACKSMITH] Brokk Ironjaw", "blacksmith_start");
    npcBlacksmith.addComponent(new Transform(200, 100));
    entities.add(npcBlacksmith);
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
    renderer.render(entities);
    renderer.renderDebug(entities);
    renderer.renderUI(entities);
  }

  private void eventHandler() {
    /*
     * Will probably add a controller context that the general game can implement
     * but different frameworks
     * define themselves just like I did with the rendering where the asset manager
     * and render context is generalized
     * in render core but the rendering-libgdx is actually defining them with that
     * framework and I use them here for test,
     * but the controller should still define the same function and parameters if
     * using libgdx or opengl, doesn't matter.
     * That is why interface is such a good way for this, to keep it general for
     * when making the game but I can define it
     * however I want in the future based on the game and so on.
     */
    // for (Entity entity : entities) {
    // entity.eventHandler();
    // }

    if (!player.hasComponent(Transform.class))
      return;
    Transform transform = player.getComponent(Transform.class);
    SpriteRenderer sprite = player.getComponent(SpriteRenderer.class);
    float speed = 200f;

    transform.velocity.zero();
    float dirX = 0;
    float dirY = 0;

    // ----- Save scum ;) -----
    if (Gdx.input.isKeyPressed(Input.Keys.F5)) {
      // BG3 shortcut for quicksave
      saveGame();
    }
    if (Gdx.input.isKeyPressed(Input.Keys.F8)) {
      // BG3 shortcut for load most recent save
      loadGame();
    }
    // ------------------------
    if (Gdx.input.isKeyPressed(Input.Keys.D)) {
      dirX += 1;
      if (sprite != null) {
        sprite.flipX = false;
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.A)) {
      dirX -= 1;
      if (sprite != null) {
        sprite.flipX = true;
      }
    }
    if (Gdx.input.isKeyPressed(Input.Keys.W))
      dirY += 1;
    if (Gdx.input.isKeyPressed(Input.Keys.S))
      dirY -= 1;

    transform.velocity.set(dirX, dirY);
    if (transform.velocity.mag() > 0) {
      transform.velocity.normalize(); // stops the diagonal speed being greater

      transform.velocity.x *= speed;
      transform.velocity.y *= speed;
    }
  }

  private void update(float delta) {
    for (Entity entity : entities) {
      entity.update(delta);
    }

    camera.update();
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
