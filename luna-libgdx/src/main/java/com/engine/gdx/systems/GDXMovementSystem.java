package com.engine.gdx.systems;

import com.engine.GameSystem;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.logic.PlayerController;

public class GDXMovementSystem implements GameSystem {
  PlayerController controller;
  Transform transform;

  public GDXMovementSystem(PlayerController controller, Transform transform) {
    this.controller = controller;
    this.transform = transform;
  }

  public void setTarget(PlayerController controller, Transform transform) {
    this.controller = controller;
    this.transform = transform;
  }

  @Override
  public void update(float deltaTime) {
    if (controller == null || transform == null)
      return;

    transform.position.x += controller.moveDir.x * controller.speed * deltaTime;
    transform.position.y += controller.moveDir.y * controller.speed * deltaTime;
  }
}
