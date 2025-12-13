package com.engine.math;

public class Vector2 {
  public float x, y;
  public Vector2(float x, float y) { this.x = x; this.y = y; }

  @Override
  public String toString() { return "(" + x + ", " + y + ")"; }
}
