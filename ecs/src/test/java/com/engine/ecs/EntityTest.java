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
    Entity.clear();
    entity = new Entity();
  }

  @Test
  void testEntityCreation() {
    Entity entity2 = new Entity();

    // id is incremental
    assertEquals(0, entity.getId());
    assertEquals(1, entity2.getId());
  }

  @Test
  void testDefaultNameGeneration() {
    assertEquals("Entity_0", entity.getName(), "Default entity should be named 'Entity_' + ID");

    Entity entity2 = new Entity(); // Should be ID 1 and name will be Entity_1
    assertEquals("Entity_1", entity2.getName(), "Subsequent entities should increment the name index");
  }

  @Test
  void testCustomNameCreation() {
    Entity blacksmith = new Entity("Blacksmith");

    assertEquals("Blacksmith", blacksmith.getName(), "Should use the provided custom name");
    assertEquals(1, blacksmith.getId());
  }

  @Test
  void testNullNameFallback() {
    Entity nullNamedEntity = new Entity(null);

    assertEquals(1, nullNamedEntity.getId());
    assertEquals("Entity_1", nullNamedEntity.getName(), "Passing null should generate a default name");
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
