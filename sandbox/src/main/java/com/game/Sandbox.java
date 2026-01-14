package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.dialogue.DialogueManager;
import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;
import com.engine.inventory.components.Inventory;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.inventory.models.ItemStack;

import java.util.ArrayList;
import java.util.List;

// QUESTION FOR SELF: maybe use Game instead of ApplicationAdapter so I can use Screen and setScreen
public class Sandbox extends ApplicationAdapter {
  private ShapeRenderer shapeRenderer;
  private OrthographicCamera camera;
  private Viewport viewport;

  private List<Entity> entities;
  private Entity player;

  private static final DialogueManager dialogueManager = new DialogueManager();

  @Override
  public void create() {
    shapeRenderer = new ShapeRenderer();
    entities = new ArrayList<>();

    camera = new OrthographicCamera();
    viewport = new FitViewport(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, camera);

    ItemRegistry.loadAllItems("/items/items.json");
    dialogueManager.loadAllGraphs("/graphs");

    player = new Entity("Tav");
    player.addComponent(new Transform(400, 300, 1));
    player.addComponent(new Inventory(10));
    player.getComponent(Inventory.class).addItem(new ItemStack("iron_sword", 1));
    entities.add(player);

    Entity npcBlacksmith = new Entity("[BLACKSMITH] Brokk Ironjaw", "blacksmith_start");
    npcBlacksmith.addComponent(new Transform(200, 100, 1));
    entities.add(npcBlacksmith);
  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
  }

  @Override
  public void render() {
    // The looping function from libGDX
    float delta = Gdx.graphics.getDeltaTime();

    eventHandler();
    update(delta);
    draw();
  }

  private void eventHandler() {
    /*
     * Same reasoning as in draw()
     *
     */
    // for (Entity entity : entities) {
    // entity.eventHandler();
    // }

    if (!player.hasComponent(Transform.class))
      return;
    Transform t = player.getComponent(Transform.class);
    float speed = 200f;

    t.velocity.zero();
    float dirX = 0;
    float dirY = 0;

    if (Gdx.input.isKeyPressed(Input.Keys.D))
      dirX += 1;
    if (Gdx.input.isKeyPressed(Input.Keys.A))
      dirX -= 1;
    if (Gdx.input.isKeyPressed(Input.Keys.W))
      dirY += 1;
    if (Gdx.input.isKeyPressed(Input.Keys.S))
      dirY -= 1;

    t.velocity.set(dirX, dirY);
    if (t.velocity.mag() > 0) {
      t.velocity.normalize(); // stops the diagonal speed being greater

      t.velocity.x *= speed;
      t.velocity.y *= speed;
    }
  }

  private void update(float delta) {
    for (Entity entity : entities) {
      entity.update(delta);
    }

    camera.update();
  }

  private void draw() {
    Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    /*
     * Will not run the rendering code inside entities
     *
     * Don't want the library to be bound to this specific application framework
     * just yet, So will try to draw things from here "over" it, on the existing
     * information from the components
     */
    // for (Entity entity : entities) {
    // entity.render();
    // }

    shapeRenderer.setProjectionMatrix(camera.combined);
    // Drawing all filled shapes to not constnatly flush GPU (keep begin and end
    // outside)
    shapeRenderer.begin(ShapeType.Filled);
    for (Entity entity : entities) {
      if (entity.hasComponent(Transform.class)) {
        Transform t = entity.getComponent(Transform.class);

        Color color;
        if (entity.getName() == "Tav") {
          color = Color.BLUE;
        } else {
          color = Color.RED;
        }

        drawRect(t.position.x, t.position.y, 10, 10, ShapeType.Line, color);
      }
    }
    shapeRenderer.end();

    // Draw all line shapes
    shapeRenderer.begin(ShapeType.Line);
    for (Entity entity : entities) {
      // Will draw all debug things (colliders and so on here later)
      // if (entity.hasComponent(Transform.class)) {
      // //
      // }
    }
    shapeRenderer.end();
  }

  private void drawRect(float x, float y, int width, int height, ShapeType type, Color color) {
    shapeRenderer.setColor(color);
    shapeRenderer.rect(x, y, width, height);
  }

  private void drawSphere(int x, int y, int width, int height, ShapeType type, Color color) {
    shapeRenderer.setColor(color);
    shapeRenderer.ellipse(x, y, width, height);
  }

  @Override
  public void dispose() {
    if (shapeRenderer != null) {
      shapeRenderer.dispose();
    }

    System.exit(0); // This fixed the Segmentation Fault due to being in WSL
  }
}
