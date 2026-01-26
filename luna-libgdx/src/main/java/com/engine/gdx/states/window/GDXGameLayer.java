package com.engine.gdx.states.window;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

public class GDXGameLayer extends WindowLayer {
  private boolean isPaused = false;

  private OrthographicCamera camera;
  private Viewport viewport;

  private GDXAssetManager assetManager;
  private GDXRender renderer;

  private GDXInputSystem inputSystem;
  private GDXMovementSystem movementSystem;
  private List<GameSystem> systems;

  private EntityManager entityManager;

  private GDXFileHandler fileHandler;
  private ECSSerializer serializer;

  private static final DialogueManager dialogueManager = new DialogueManager();

  public GDXGameLayer(int width, int height, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.assetManager = assetManager;
  }

  @Override
  public String getId() {
    return "GAME_LAYER";
  }

  @Override
  public void enter() {
    entityManager = new EntityManager();
    systems = new ArrayList<>();

    camera = new OrthographicCamera();
    viewport = new FitViewport(this.width, this.height, camera);
    // Have to force viewport update when entering new Layer:
    viewport.update(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), true);

    renderer = new GDXRender(assetManager, camera);

    ItemRegistry.loadAllItems("/items/items.json");
    dialogueManager.loadAllGraphs("/graphs");

    // Game systems:
    // Have input system still stored normally because I want to access keybindings
    inputSystem = new GDXInputSystem(entityManager);
    movementSystem = new GDXMovementSystem(entityManager);

    systems.add(inputSystem);
    systems.add(new RenderSystem(renderer, entityManager, true));
    systems.add(movementSystem);

    fileHandler = new GDXFileHandler();
    serializer = new ECSSerializer();
    loadGame();
  }

  @Override
  public void exit() {
    if (renderer != null) {
      renderer.dispose();
    }
    if (assetManager != null) {
      assetManager.dispose();
    }
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
    renderer.resize(width, height);
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
