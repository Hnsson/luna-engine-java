package com.engine.ecs.components.physics;

import com.engine.ecs.Component;

public class BoxCollider extends Component {
  public int width;
  public int height;

  public int offsetX = 0;
  public int offsetY = 0;

  public BoxCollider() {
    this(0, 0, 0, 0);
  }

  public BoxCollider(int width, int height) {
    this.width = width;
    this.height = height;
  }

  public BoxCollider(int width, int height, int offsetX, int offsetY) {
    this.width = width;
    this.height = height;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }
}
