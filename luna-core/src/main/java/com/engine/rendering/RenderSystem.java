package com.engine.rendering;

import java.util.List;

import com.engine.GameSystem;
import com.engine.SystemContext;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.physics.BoxCollider;
import com.engine.ecs.components.physics.CircleCollider;
import com.engine.rendering.components.SpriteRenderer;
import com.engine.rendering.contexts.IRenderContext;
import com.engine.rendering.logic.SpriteRegistry;
import com.engine.rendering.models.SpriteDefinition;

public class RenderSystem implements GameSystem {
  private final IRenderContext context;
  private EntityManager entityManager;
  private boolean debugMode = false;

  public RenderSystem(IRenderContext context, EntityManager entityManager) {
    this.context = context; // Get the framework-specific implementation of the interface (libgdx, opengl,
                            // ...)
    this.entityManager = entityManager;
  }

  public RenderSystem(IRenderContext context, EntityManager entityManager, boolean debugMode) {
    this.context = context;
    this.entityManager = entityManager;
    this.debugMode = debugMode;
  }

  @Override
  public void render() {
    context.clearScreen(0.1f, 0.1f, 0.1f);

    renderEntities();

    if (debugMode)
      renderDebug();

    renderUI();
  }

  public void toggleDebugMode() {
    this.debugMode = !debugMode;
  }

  public void renderEntities() {
    context.beginFrame();
    List<Entity> entities = entityManager.getEntitiesWith(Transform.class, SpriteRenderer.class);

    // Based on entity layer order (smallest to largest):
    entities.sort((e1, e2) -> {
      int layer1 = RenderLayerRegistry.getOrder(e1.getLayer());
      int layer2 = RenderLayerRegistry.getOrder(e2.getLayer());
      return Integer.compare(layer1, layer2);
    });

    for (Entity entity : entities) {
      if (!entity.hasComponent(Transform.class))
        continue;

      Transform transform = entity.getComponent(Transform.class);

      float width = 0, height = 0;
      float xOffset = 0, yOffset = 0;

      if (entity.hasComponent(SpriteRenderer.class)) {
        SpriteRenderer sprite = entity.getComponent(SpriteRenderer.class);
        SpriteDefinition def = SpriteRegistry.get(sprite.spriteId);

        if (def != null) {
          width = sprite.width;
          height = sprite.height;
          xOffset = sprite.xOffset;
          yOffset = sprite.yOffset;

          float drawX = transform.position.x - (width / 2f) + xOffset;
          float drawY = transform.position.y + yOffset;

          context.drawSprite(def, sprite.currentFrameIndex, drawX, drawY,
              width, height, sprite.flipX, sprite.flipY);
        } else {
          System.err.println("[ERROR::RENDERSYSTEM] Sprite defintion not found");
        }
      }

      // Render the names above all transforms (also based on sprite)
      String name = entity.getName();
      if (name != null && !name.isEmpty()) {
        float textWidth = context.getTextWidth(name);

        float centeredX = transform.position.x + xOffset - (textWidth / 2f);
        float centeredY = transform.position.y + yOffset + height + 20;

        context.drawText(name, centeredX, centeredY, 1, 1, 1, 1);
      }
    }
    context.endFrame();
  }

  public void renderDebug() {
    context.beginDebugFrame();
    List<Entity> entities = entityManager.getEntitiesWith(Transform.class, SpriteRenderer.class);

    for (Entity entity : entities) {
      if (!entity.hasComponent(Transform.class))
        continue;
      Transform transform = entity.getComponent(Transform.class);

      // Draw a red transform rect to spot it more easily to see the point which every
      // other component's position is based on
      context.drawFilledRectangle(transform.position.x - 2, transform.position.y - 2, 4, 4, 1, 0, 0, 1);

      // Draw a green box on coliders
      if (entity.hasComponent(BoxCollider.class)) {
        BoxCollider boxCollider = entity.getComponent(BoxCollider.class);
        context.drawRectangle(transform.position.x + boxCollider.offsetX, transform.position.y + boxCollider.offsetY,
            boxCollider.width,
            boxCollider.height, 0, 1,
            0, 1);
      }
      if (entity.hasComponent(CircleCollider.class)) {
        CircleCollider circleCollider = entity.getComponent(CircleCollider.class);
        context.drawCircle(transform.position.x + circleCollider.offsetX, transform.position.y + circleCollider.offsetY,
            circleCollider.radius, 0, 1, 0, 1);
      }

      // Draw a blue box on sprites
      if (entity.hasComponent(SpriteRenderer.class)) {
        SpriteRenderer sprite = entity.getComponent(SpriteRenderer.class);

        float drawX = transform.position.x - (sprite.width / 2f) + sprite.xOffset;
        float drawY = transform.position.y + sprite.yOffset;
        context.drawRectangle(drawX, drawY, sprite.width, sprite.height, 0, 0, 1, 1);
      }
    }

    context.endDebugFrame();
  }

  public void renderUI() {
    context.beginUIFrame();

    /*
     * Have like how Unity does it where a UIComponent is attached to entities and
     * draw them, like the player may have like a health bar which will be rendered
     * here.
     */

    context.endUIFrame();
  }
}
