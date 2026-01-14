package com.engine.ecs.components;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TransformTest {

  @Test
  void testDefaultConstructor() {
    Transform t = new Transform();

    assertNotNull(t.position, "Position vector should be initialized");
    assertNotNull(t.velocity, "Velocity vector should be initialized");

    assertEquals(0, t.position.x);
    assertEquals(0, t.position.y);
    assertEquals(1, t.scale); // Default scale is 1
  }

  @Test
  void testConstructorWithScale() {
    Transform t = new Transform(5);

    assertEquals(5, t.scale);
    assertEquals(0, t.position.x); // Should still be zero
    assertNotNull(t.position); // Should still be allocated
  }

  @Test
  void testConstructorWithCoordinates() {
    Transform t = new Transform(10.5f, -20.0f);

    assertEquals(10.5f, t.position.x);
    assertEquals(-20.0f, t.position.y);
    assertEquals(1, t.scale);
  }

  @Test
  void testFullConstructor() {
    Transform t = new Transform(100f, 200f, 10);

    assertEquals(100f, t.position.x);
    assertEquals(200f, t.position.y);
    assertEquals(10, t.scale);
  }

  @Test
  void testUpdateMovesPositionByVelocity() {
    Transform t = new Transform(0, 0);

    t.velocity.x = 5.0f;
    t.velocity.y = 2.0f;

    t.update(1); // Go one frame forward

    assertEquals(5.0f, t.position.x, "X position should increase by X velocity");
    assertEquals(2.0f, t.position.y, "Y position should increase by Y velocity");

    t.update(1); // ...

    assertEquals(10.0f, t.position.x);
    assertEquals(4.0f, t.position.y);
  }

  @Test
  void testToString() {
    Transform t = new Transform(1.5f, 2.5f);
    String output = t.toString();

    assertEquals("(1.5, 2.5)", output);
  }
}
