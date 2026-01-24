package com.engine.ecs.components;

import com.engine.ecs.Component;
import com.engine.math.Vector2f;

public class Transform extends Component {
  public Vector2f position = new Vector2f();
  public Vector2f velocity = new Vector2f();

  public Transform() {
    this(0, 0);
  }

  public Transform(float x, float y) {
    this.position.x = x;
    this.position.y = y;
  }

  @Override
  public String toString() {
    return "(" + this.position.x + ", " + this.position.y + ")";
  }

  @Override
  public void init() {
  }

  @Override
  public void update(float deltaTime) {
    this.position.x += this.velocity.x * deltaTime;
    this.position.y += this.velocity.y * deltaTime;
  }

  @Override
  public void render() {
  }
}
