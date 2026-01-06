package com.engine.ecs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;

class ComponentRegistryTest {
  static class CompA extends Component {
  }

  static class CompB extends Component {
  }

  @AfterEach
  void reset() {
    ComponentRegistry.clear();
  }

  @Test
  void testIDsAreIncremental() {
    int id1 = ComponentRegistry.getComponentTypeID(CompA.class);
    int id2 = ComponentRegistry.getComponentTypeID(CompB.class);

    assertEquals(0, id1, "First component should get ID 0");
    assertEquals(1, id2, "Second component should get ID 1");
  }

  @Test
  void testSameClassReturnsSameID() {
    int id1 = ComponentRegistry.getComponentTypeID(CompA.class);
    int id2 = ComponentRegistry.getComponentTypeID(CompA.class);

    assertEquals(id1, id2, "ID should remain consistent for the same component");
  }
}
