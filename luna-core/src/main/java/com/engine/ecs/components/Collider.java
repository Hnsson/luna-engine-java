package com.engine.ecs.components;

import com.engine.ecs.Component;

public abstract class Collider extends Component {
  public int width, height, offsetX = 0, offsetY = 0;

  public boolean isTrigger = false;

  public Collider() {
  }

  public Collider(int width, int height, int offsetX, int offsetY, boolean isTrigger) {
    this.width = width;
    this.height = height;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
    this.isTrigger = isTrigger;
  }
}
