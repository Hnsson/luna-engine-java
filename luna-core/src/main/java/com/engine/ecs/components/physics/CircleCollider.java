package com.engine.ecs.components.physics;

import com.engine.ecs.components.Collider;

public class CircleCollider extends Collider {
  public float radius;

  public CircleCollider() {
    this(0);
  }

  public CircleCollider(float radius) {
    this(radius, false);
  }

  public CircleCollider(float radius, boolean isTrigger) {
    this(radius, 0, 0, isTrigger);
  }

  public CircleCollider(float radius, int offsetX, int offsetY, boolean isTrigger) {
    super(0, 0, offsetX, offsetY, isTrigger);
    this.radius = radius;
  }
}
