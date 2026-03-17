package com.engine.ecs.components;

import com.engine.ecs.Component;
import com.engine.math.Vector2f;

public class Transform extends Component {
  public Vector2f position = new Vector2f();

  public Transform() {
    this(0, 0);
  }

  public Transform(float x, float y) {
    this.position.x = x;
    this.position.y = y;
  }

  @Override
  public String toString() {
    // wanted to round to 2 decimals, was cluttering the screen before
    float roundedX = Math.round(this.position.x * 100.0f) / 100.0f;
    float roundedY = Math.round(this.position.y * 100.0f) / 100.0f;
    return "(" + roundedX + ", " + roundedY + ")";
  }
}
