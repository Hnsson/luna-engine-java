package com.engine.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vector2fTest {

  @Test
  void testVectorCreation() {
    Vector2f vec = new Vector2f(10.0f, 5.0f);

    assertEquals(10.0f, vec.x, "X should be 10.0");
    assertEquals(5.0f, vec.y, "Y should be 5.0");
  }

  @Test
  void testToString() {
    Vector2f vec = new Vector2f(10.0f, 5.0f);

    assertEquals("(10.0, 5.0)", vec.toString());
  }
}
