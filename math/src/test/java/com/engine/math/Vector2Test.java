package com.engine.math;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class Vector2Test {

  @Test
  void testVectorCreation() {
    Vector2 vec = new Vector2(10.0f, 5.0f);

    assertEquals(11.0f, vec.x, "X should be 10.0");
    assertEquals(5.0f, vec.y, "Y should be 5.0");
  }

  @Test
  void testToString() {
    Vector2 vec = new Vector2(10.0f, 5.0f);

    assertEquals("(10.0, 5.0)", vec.toString());
  }
}
