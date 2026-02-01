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
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.GDXScriptContext;
import com.engine.gdx.io.GDXFileHandler;
import com.engine.gdx.rendering.GDXAssetManager;
import com.engine.gdx.rendering.GDXRender;
import com.engine.gdx.systems.GDXCollisionSystem;
import com.engine.gdx.systems.GDXDialogueSystem;
import com.engine.gdx.systems.GDXInputSystem;
import com.engine.gdx.systems.GDXMovementSystem;
import com.engine.gdx.systems.GDXScriptSystem;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.rendering.RenderSystem;

public class GDXGameLayer extends WindowLayer {
  private String levelName;
  private boolean isPaused = false;
  private Entity player;

  private OrthographicCamera camera;
  private Viewport viewport;

  private GDXAssetManager assetManager;
  private GDXRender renderer;

  private GDXInputSystem inputSystem;
  private RenderSystem renderSystem;
  private GDXMovementSystem movementSystem;
  private GDXDialogueSystem dialogueSystem;
  private GDXCollisionSystem collisionSystem;
  private GDXScriptSystem scriptSystem;
  private List<GameSystem> systems;

  private GDXScriptContext scriptContext;

  private EntityManager entityManager;

  private GDXFileHandler fileHandler;
  private ECSSerializer serializer;

  public GDXGameLayer(int width, int height, String levelName, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.levelName = levelName;
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

    // Game systems:
    // Have input system still stored normally because I want to access keybindings
    inputSystem = new GDXInputSystem(entityManager);
    renderSystem = new RenderSystem(renderer, entityManager, false);
    movementSystem = new GDXMovementSystem(entityManager);
    dialogueSystem = new GDXDialogueSystem(levelName, new DialogueManager(), renderer, inputSystem);
    collisionSystem = new GDXCollisionSystem(entityManager);

    scriptContext = new GDXScriptContext(entityManager, inputSystem, dialogueSystem, renderer);
    scriptSystem = new GDXScriptSystem(scriptContext);

    systems.add(inputSystem);
    systems.add(renderSystem);
    systems.add(movementSystem);
    systems.add(dialogueSystem);
    systems.add(collisionSystem);
    systems.add(scriptSystem);

    fileHandler = new GDXFileHandler();
    serializer = new ECSSerializer();
    loadGame(levelName);
    // REMOVE THIS LATER WHEN IMPLEMENTING SMART WAY TO START DIALOGUE,
    // THIS JUST TEST WORK FOR LEVEL ONE!!!!
    // dialogueSystem.startDialogue(entityManager.getEntity(0),
    // entityManager.getEntity(1));
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
      if (dialogueSystem.isActive()) {
        dialogueSystem.update(delta);
        // Add other systems that should also work during conversation
      } else {
        for (GameSystem system : systems) {
          system.update(delta);
        }
      }
    }
    camera.update();
  }

  @Override
  public void eventHandler() {
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.PAUSE))) {
      isPaused = !isPaused;
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.DEBUG))) {
      renderSystem.toggleDebugMode();
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.SAVE))) {
      saveGame();
    }
    if (Gdx.input.isKeyJustPressed(inputSystem.getKeybind(GDXInputSystem.Mapping.LOAD))) {
      loadGame(levelName);
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
    entityManager.saveEntities("saves/levels/" + levelName + "/entities.json", fileHandler, serializer, this.player);
    // Save the player state
    entityManager.saveEntity("saves/player.json", this.player, fileHandler, serializer);
    System.out.println("Saved game");
  }

  private void loadGame(String levelName) {
    entityManager.loadEntities("saves/levels/" + levelName + "/entities.json", fileHandler, serializer, true);
    this.player = entityManager.loadEntity("saves/player.json", fileHandler, serializer, false);
    System.out.println("Game loaded");
  }
}
