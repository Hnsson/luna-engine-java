package com.engine.ecs.components.physics;

import com.engine.ecs.components.Collider;

public class BoxCollider extends Collider {
  public BoxCollider() {
    this(0, 0);
  }

  public BoxCollider(int width, int height) {
    this(width, height, 0, 0, false);
  }

  public BoxCollider(int width, int height, int offsetX, int offsetY, boolean isTrigger) {
    super(width, height, offsetX, offsetY, isTrigger);
  }

}
