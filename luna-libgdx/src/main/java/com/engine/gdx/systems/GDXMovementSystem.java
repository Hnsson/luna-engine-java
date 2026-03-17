package com.engine.gdx.systems;

import java.util.List;

import com.engine.GameSystem;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.logic.CharacterController;
import com.engine.ecs.components.physics.RigidBody;

public class GDXMovementSystem implements GameSystem {
  private EntityManager entityManager;

  public GDXMovementSystem(EntityManager entityManager) {
    this.entityManager = entityManager;
  }

  @Override
  public void update(float delta) {
    List<Entity> entities = entityManager.getEntitiesWithAll(CharacterController.class, RigidBody.class,
        Transform.class);

    for (Entity entity : entities) {
      CharacterController controller = entity.getComponent(CharacterController.class);
      RigidBody rb = entity.getComponent(RigidBody.class);
      Transform transform = entity.getComponent(Transform.class);

      if (controller != null && rb != null && transform != null) {
        controller.moveDir.normalize();
        rb.velocity.x = controller.moveDir.x * controller.speed;
        rb.velocity.y = controller.moveDir.y * controller.speed;

        transform.position.x += rb.velocity.x * delta;
        transform.position.y += rb.velocity.y * delta;
      }
    }
  }
}
