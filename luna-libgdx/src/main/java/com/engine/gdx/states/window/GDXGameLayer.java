package com.engine.gdx.states.window;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.GameSystem;
import com.engine.dialogue.DialogueManager;
import com.engine.ecs.ECSSerializer;
import com.engine.ecs.EntityManager;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.io.GDXFileHandler;
import com.engine.gdx.rendering.GDXAssetManager;
import com.engine.gdx.rendering.GDXRender;
import com.engine.gdx.systems.GDXInputSystem;
import com.engine.gdx.systems.GDXMovementSystem;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.rendering.RenderSystem;
import com.engine.rendering.logic.SpriteRegistry;
import com.engine.rendering.models.SpriteDefinition;

public class GDXGameLayer extends WindowLayer {
  private boolean isPaused = false;

  private ShapeRenderer shapeRenderer;
  private OrthographicCamera camera;
  private Viewport viewport;

  private GDXAssetManager assetManager;
  private GDXRender renderContext;

  private GDXInputSystem inputSystem;
  private GDXMovementSystem movementSystem;
  private List<GameSystem> systems;

  private EntityManager entityManager;

  private GDXFileHandler fileHandler;
  private ECSSerializer serializer;

  private static final DialogueManager dialogueManager = new DialogueManager();

  public GDXGameLayer(int width, int height) {
    super(width, height); // pass it up
  }

  @Override
  public String getId() {
    return "GAME_LAYER";
  }

  @Override
  public void enter() {
    shapeRenderer = new ShapeRenderer();

    entityManager = new EntityManager();
    systems = new ArrayList<>();

    camera = new OrthographicCamera();
    viewport = new FitViewport(this.width, this.height, camera);

    assetManager = new GDXAssetManager();
    renderContext = new GDXRender(assetManager, camera);

    ItemRegistry.loadAllItems("/items/items.json");
    dialogueManager.loadAllGraphs("/graphs");
    assetManager.loadTexture("sprites/playerIdle.png");
    SpriteRegistry.register("playerIdle", new SpriteDefinition("sprites/playerIdle.png", 0, 0, 192, 192));

    // Game systems:
    // Have input system still stored normally because I want to access keybindings
    inputSystem = new GDXInputSystem(entityManager);
    movementSystem = new GDXMovementSystem(entityManager);

    systems.add(inputSystem);
    systems.add(new RenderSystem(renderContext, entityManager, true));
    systems.add(movementSystem);

    fileHandler = new GDXFileHandler();
    serializer = new ECSSerializer();
    loadGame();
  }

  @Override
  public void exit() {
    if (shapeRenderer != null) {
      shapeRenderer.dispose();
    }
    if (renderContext != null) {
      renderContext.dispose();
    }
    if (assetManager != null) {
      assetManager.dispose();
    }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
    renderContext.resize(width, height);
  }

  @Override
  public void render() {
    for (GameSystem system : systems) {
      system.render();
    }
  }

  @Override
  public void update(float delta) {
    if (!isPaused) {
      for (GameSystem system : systems) {
        system.update(delta);
      }
    }
    camera.update();
  }

  @Override
  public void eventHandler() {
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.PAUSE))) {
      isPaused = !isPaused;
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.SAVE))) {
      saveGame();
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.LOAD))) {
      loadGame();
    }

    for (GameSystem system : systems) {
      system.eventHandler();
    }
  }

  private void saveGame() {
    // In the future a state will have a state name (Level name)
    // That will append that to the file so I can easily load and save
    // levels based on the state name they got.
    // for example: "saves/" + this.stateId + ".json"
    entityManager.saveEntities("saves/entities.json", fileHandler, serializer);
    System.out.println("Saved game");
  }

  private void loadGame() {
    entityManager.loadEntities("saves/entities.json", fileHandler, serializer, true);
    System.out.println("Game loaded");
  }
}
