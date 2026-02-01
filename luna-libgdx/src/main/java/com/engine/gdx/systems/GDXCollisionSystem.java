package com.engine.gdx.systems;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Rectangle;
import com.engine.GameSystem;
import com.engine.ecs.Component;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.Collider;
import com.engine.ecs.components.physics.BoxCollider;
import com.engine.ecs.components.physics.RigidBody;
import com.engine.gdx.Script;

public class GDXCollisionSystem implements GameSystem {
  private EntityManager entityManager;

  // Add some kind of memory so I know to call onTriggerLeave
  private Set<String> activeCollisions;
  private Set<String> previousCollisions;

  // read that object reusing can help with performance with the garbace
  // collector, so instead of creating new rectangles every check I can have
  // rectangles already defined and I just change them based on the entities
  // (https://foojay.io/today/how-object-reuse-can-reduce-latency-and-improve-performance/),
  // don't know if necessary but felt like a cool way to test it
  private static final Rectangle rectA = new Rectangle();
  private static final Rectangle rectB = new Rectangle();

  public GDXCollisionSystem(EntityManager entityManager) {
    this.entityManager = entityManager;
    this.activeCollisions = new HashSet<>();
    this.previousCollisions = new HashSet<>();
  }

  @Override
  public void update(float delta) {
    // swap from last frame
    previousCollisions.clear();
    previousCollisions.addAll(activeCollisions);
    activeCollisions.clear();

    List<Entity> entities = entityManager.getEntitiesWith(Transform.class, Collider.class, RigidBody.class);

    for (int i = 0; i < entities.size(); i++) {
      // Start at i+1 so it doesnt check with itself
      Entity e1 = entities.get(i);
      Collider c1 = e1.getComponent(Collider.class);
      if (c1 == null)
        continue;

      for (int j = i + 1; j < entities.size(); j++) {
        Entity e2 = entities.get(j);
        Collider c2 = e2.getComponent(Collider.class);
        if (c2 == null)
          continue;

        if (isColliding(e1, e2)) {
          String collisionKey = createKey(e1, e2);
          activeCollisions.add(collisionKey);
          resolve(e1, e2);
        }
      }
    }

    handleTriggers(entities);
  }

  private String createKey(Entity e1, Entity e2) {
    int id1 = e1.getId(), id2 = e2.getId();

    if (id1 < id2) {
      return id1 + ":" + id2;
    } else {
      return id2 + ":" + id1;
    }
  }

  private boolean isColliding(Transform t1, Collider c1, Transform t2, Collider c2) {
    if (c1 instanceof BoxCollider && c2 instanceof BoxCollider) {
      rectA.set(t1.position.x + c1.offsetX, t1.position.y + c1.offsetY, c1.width, c1.height);
      rectB.set(t2.position.x + c2.offsetX, t2.position.y + c2.offsetY, c2.width, c2.height);
      return rectA.overlaps(rectB);
    }

    return false;
  }

  private boolean isColliding(Entity e1, Entity e2) {
    Transform t1 = e1.getComponent(Transform.class);
    Collider c1 = e1.getComponent(Collider.class);

    Transform t2 = e2.getComponent(Transform.class);
    Collider c2 = e2.getComponent(Collider.class);

    return isColliding(t1, c1, t2, c2);
  }

  private void handleTriggers(List<Entity> entities) {
    // handle onTriggerEnter
    for (String collisionKey : activeCollisions) {
      if (!previousCollisions.contains(collisionKey)) {
        Entity[] pair = getEntitiesFromKey(collisionKey, entities);
        if (pair != null) {
          // Trigger each others script so entity1 gets entity2 in trigger and the other
          // way around
          triggerCollision(pair[0], pair[1], true);
          triggerCollision(pair[1], pair[0], true);
        }
      }
    }
    // handle onTriggerLeave
    for (String collisionKey : previousCollisions) {
      if (!activeCollisions.contains(collisionKey)) {
        Entity[] pair = getEntitiesFromKey(collisionKey, entities);
        if (pair != null) {
          triggerCollision(pair[0], pair[1], false);
          triggerCollision(pair[1], pair[0], false);
        }
      }
    }

  }

  private Entity[] getEntitiesFromKey(String collisionKey, List<Entity> entities) {
    String[] ids = collisionKey.split(":");
    int id1 = Integer.parseInt(ids[0]);
    int id2 = Integer.parseInt(ids[1]);

    Entity e1 = null, e2 = null;
    for (Entity e : entities) {
      if (e.getId() == id1)
        e1 = e;
      if (e.getId() == id2)
        e2 = e;
      if (e1 != null && e2 != null)
        return new Entity[] { e1, e2 };
    }
    return null;
  }

  private void triggerCollision(Entity owner, Entity trigger, boolean enter) {
    List<Component> components = owner.getComponents();

    for (Component cmp : components) {
      if (cmp instanceof Script) {
        Script script = (Script) cmp;

        if (enter) {
          script.onTriggerEnter(trigger);
        } else {
          script.onTriggerLeave(trigger);
        }
      }
    }
  }

  private void resolve(Entity e1, Entity e2) {
    if (!e1.hasComponent(RigidBody.class) || !e2.hasComponent(RigidBody.class))
      return;

    Transform t1 = e1.getComponent(Transform.class);
    Collider c1 = e1.getComponent(Collider.class);
    RigidBody r1 = e1.getComponent(RigidBody.class);

    Transform t2 = e2.getComponent(Transform.class);
    Collider c2 = e2.getComponent(Collider.class);
    RigidBody r2 = e2.getComponent(RigidBody.class);

    float x1 = t1.position.x + c1.offsetX, y1 = t1.position.y + c1.offsetY;
    float x2 = t2.position.x + c2.offsetX, y2 = t2.position.y + c2.offsetY;

    float t1Share = 0f;
    float t2Share = 0f;

    if (r1.immovable) {
      t1Share = 0f; // immovable moves 0%
      t2Share = 1f; // movable moves 100% of the overlap
    } else if (r2.immovable) {
      t1Share = 1f;
      t2Share = 0f;
    } else {
      float totalMass = r1.mass + r2.mass;
      if (totalMass == 0) {
        t1Share = 0.5f;
        t2Share = 0.5f;
      } else {
        t1Share = r2.mass / totalMass;
        t2Share = r1.mass / totalMass;
      }
    }
    float overlapX = Math.min(x1 + c1.width, x2 + c2.width) - Math.max(x1, x2);
    float overlapY = Math.min(y1 + c1.height, y2 + c2.height) - Math.max(y1, y2);

    if (overlapX < overlapY) {
      if (x1 < x2) {
        t1.position.x -= overlapX * t1Share;
        t2.position.x += overlapX * t2Share;
      } else {
        t1.position.x += overlapX * t1Share;
        t2.position.x -= overlapX * t2Share;
      }
      if (!r1.immovable)
        t1.velocity.x = 0;
      if (!r2.immovable)
        t2.velocity.x = 0;
    } else {
      if (y1 < y2) {
        t1.position.y -= overlapY * t1Share;
        t2.position.y += overlapY * t2Share;
      } else {
        t1.position.y += overlapY * t1Share;
        t2.position.y -= overlapY * t2Share;
      }

      if (!r1.immovable)
        t1.velocity.y = 0;
      if (!r2.immovable)
        t2.velocity.y = 0;
    }
  }
}
