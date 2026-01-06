package com.engine.ecs;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;

import com.engine.ecs.components.*;

class EntityTest {
  private Entity entity;

  @BeforeEach
  void setUp() {
    // Reset the entity before every single test
    entity = new Entity(1);
  }

  @Test
  void testEntityCreation() {
    Entity e1 = new Entity();
    Entity e2 = new Entity(42);

    // id defaults to 0 if not provided
    assertEquals(0, e1.getID());
    assertEquals(42, e2.getID());
  }

  @Test
  void testAddComponent() {
    Transform t_1 = new Transform();
    Transform t_2 = entity.addComponent(t_1);

    assertNotNull(t_2);
    assertEquals(t_2, t_1);

    assertTrue(entity.hasComponent(Transform.class));
  }

  @Test
  void testGetComponent() {
    Transform t_1 = new Transform();
    // t_1.velocity.x = 10f

    entity.addComponent(t_1);

    Transform t_2 = entity.getComponent(Transform.class);

    assertNotNull(t_2);

    t_2.velocity.x = 11f;
    assertEquals(11f, entity.getComponent(Transform.class).velocity.x);
  }

  @Test
  void testGetMissingComponent() {
    assertFalse(entity.hasComponent(Transform.class));
    assertNull(entity.getComponent(Transform.class));
  }

  @Test
  void testAddDuplicateComponent() {
    Transform transform = new Transform(100, 200);
    entity.addComponent(transform);

    Transform transform2 = new Transform(200, 300);

    Transform result = entity.addComponent(transform2);

    assertSame(transform, result, "Should return the existing component instance");
    assertNotSame(transform2, result, "Should not accept the new component instance");

    assertEquals(100, entity.getComponent(Transform.class).position.x);
  }
}
