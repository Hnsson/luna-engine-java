package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import com.engine.dialogue.DialogueManager;
import com.engine.ecs.EntityManager;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.rendering.RenderSystem;
import com.engine.GameSystem;
import com.engine.gdx.io.GDXFileHandler;
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

  private GDXInputSystem inputSystem;
  private GDXMovementSystem movementSystem;
  private List<GameSystem> systems;

  private GDXFileHandler fileHandler;
  private ECSSerializer serializer;
  private EntityManager entityManager;

  private static final DialogueManager dialogueManager = new DialogueManager();

  @Override
  public void create() {
    shapeRenderer = new ShapeRenderer();

    entityManager = new EntityManager();
    systems = new ArrayList<>();

    camera = new OrthographicCamera();
    viewport = new FitViewport(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, camera);

    assetManager = new GDXAssetManager();
    renderContext = new GDXRender(assetManager, camera);

    ItemRegistry.loadAllItems("/items/items.json");
    dialogueManager.loadAllGraphs("/graphs");
    assetManager.loadTexture("sprites/playerIdle.png");
    SpriteRegistry.register("playerIdle", new SpriteDefinition("sprites/playerIdle.png", 0, 0, 192, 192));

    fileHandler = new GDXFileHandler();
    serializer = new ECSSerializer();
    loadGame();

    // Game systems:
    // Have input system still stored normally because I want to access keybindings
    inputSystem = new GDXInputSystem(entityManager);
    movementSystem = new GDXMovementSystem(entityManager);

    systems.add(inputSystem);
    systems.add(new RenderSystem(renderContext, entityManager, true));
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
