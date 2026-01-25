package com.engine.gdx.systems;

import java.util.List;

import com.engine.GameSystem;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.logic.PlayerController;

public class GDXMovementSystem implements GameSystem {
  private EntityManager entityManager;

  public GDXMovementSystem(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void update(float deltaTime) {
    List<Entity> entities = entityManager.getEntitiesWith(PlayerController.class, Transform.class);

    for (Entity entity : entities) {
      PlayerController controller = entity.getComponent(PlayerController.class);
      Transform transform = entity.getComponent(Transform.class);

      if (controller != null && transform != null) {
        transform.position.x += controller.moveDir.x * controller.speed * deltaTime;
        transform.position.y += controller.moveDir.y * controller.speed * deltaTime;
      }
    }
  }
}
