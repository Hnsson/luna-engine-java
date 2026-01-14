package com.engine.math;

public class Vector2f {
  public float x, y;

  public Vector2f() {
    this(0, 0);
  }

  public Vector2f(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public Vector2f add(Vector2f vec) {
    this.x += vec.x;
    this.y += vec.y;
    return this;
  }

  public Vector2f sub(Vector2f vec) {
    this.x -= vec.x;
    this.y -= vec.y;
    return this;
  }

  public Vector2f mult(Vector2f vec) {
    this.x *= vec.x;
    this.y *= vec.y;
    return this;
  }

  public Vector2f div(Vector2f vec) {
    this.x /= vec.x;
    this.y /= vec.y;
    return this;
  }

  public Vector2f mult(float i) {
    this.x *= i;
    this.y *= i;
    return this;
  }

  public Vector2f zero() {
    this.x = 0;
    this.y = 0;
    return this;
  }

  public void set(float x, float y) {
    this.x = x;
    this.y = y;
  }

  public void normalize() {
    float magnitude = mag();
    if (magnitude != 0) {
      this.x /= magnitude;
      this.y /= magnitude;
    }
  }

  public float mag() {
    return (float) Math.sqrt((x * x) + (y * y));
  }

  @Override
  public String toString() {
    return "(" + x + ", " + y + ")";
  }
}
