package com.engine.gdx.systems;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.engine.GameSystem;
import com.engine.ecs.Component;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.Collider;
import com.engine.ecs.components.physics.BoxCollider;
import com.engine.ecs.components.physics.CircleCollider;
import com.engine.ecs.components.physics.RigidBody;
import com.engine.gdx.CollisionPair;
import com.engine.Script;

public class GDXCollisionSystem implements GameSystem {
  private EntityManager entityManager;

  // Add some kind of memory so I know to call onTriggerLeave
  private Set<CollisionPair> activeCollisions;
  private Set<CollisionPair> previousCollisions;

  // read that object reusing can help with performance with the garbace
  // collector, so instead of creating new rectangles every check I can have
  // rectangles already defined and I just change them based on the entities
  // (https://foojay.io/today/how-object-reuse-can-reduce-latency-and-improve-performance/),
  // don't know if necessary but felt like a cool way to test it
  private static final Rectangle rectA = new Rectangle();
  private static final Rectangle rectB = new Rectangle();
  private static final Circle circleA = new Circle();
  private static final Circle circleB = new Circle();

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
      List<Collider> e1Colliders = getColliders(e1);
      if (e1Colliders.isEmpty())
        continue;

      for (int j = i + 1; j < entities.size(); j++) {
        Entity e2 = entities.get(j);
        List<Collider> e2Colliders = getColliders(e2);
        if (e2Colliders.isEmpty())
          continue;

        // Need to check for collision between all the entities colliders also
        for (Collider c1 : e1Colliders) {
          for (Collider c2 : e2Colliders) {
            if (isColliding(e1, c1, e2, c2)) {
              if (c1.isTrigger || c2.isTrigger)
                activeCollisions.add(new CollisionPair(e1, e2));
              if (!c1.isTrigger && !c2.isTrigger)
                resolve(e1, e2);
            }
          }
        }
      }
    }

    handleTriggers();
  }

  private boolean checkCollision(Transform t1, Collider c1, Transform t2, Collider c2) {
    if (c1 instanceof BoxCollider && c2 instanceof BoxCollider) {
      rectA.set(t1.position.x + c1.offsetX, t1.position.y + c1.offsetY, c1.width, c1.height);
      rectB.set(t2.position.x + c2.offsetX, t2.position.y + c2.offsetY, c2.width, c2.height);
      return rectA.overlaps(rectB);
    }

    if (c1 instanceof CircleCollider && c2 instanceof BoxCollider) {
      CircleCollider circleC1 = (CircleCollider) c1;
      circleA.set(t1.position.x + circleC1.offsetX, t1.position.y + circleC1.offsetY, circleC1.radius);
      rectB.set(t2.position.x + c2.offsetX, t2.position.y + c2.offsetY, c2.width, c2.height);
      return Intersector.overlaps(circleA, rectB);
    }

    if (c1 instanceof BoxCollider && c2 instanceof CircleCollider) {
      CircleCollider circleC2 = (CircleCollider) c2;
      rectA.set(t1.position.x + c1.offsetX, t1.position.y + c1.offsetY, c1.width, c1.height);
      circleB.set(t2.position.x + circleC2.offsetX, t2.position.y + circleC2.offsetY, circleC2.radius);
      return Intersector.overlaps(circleB, rectA);
    }

    if (c1 instanceof CircleCollider && c2 instanceof CircleCollider) {
      CircleCollider circleC1 = (CircleCollider) c1;
      CircleCollider circleC2 = (CircleCollider) c2;
      circleA.set(t1.position.x + circleC1.offsetX, t1.position.y + circleC1.offsetY, circleC1.radius);
      circleB.set(t2.position.x + circleC2.offsetX, t2.position.y + circleC2.offsetY, circleC2.radius);
      return circleA.overlaps(circleB);
    }

    return false;
  }

  private boolean isColliding(Entity e1, Collider c1, Entity e2, Collider c2) {
    Transform t1 = e1.getComponent(Transform.class);
    Transform t2 = e2.getComponent(Transform.class);

    return checkCollision(t1, c1, t2, c2);
  }

  private List<Collider> getColliders(Entity e) {
    List<Collider> colliders = new ArrayList<>();
    for (Component cmp : e.getComponents()) {
      if (cmp instanceof Collider) {
        Collider colliderCmp = (Collider) cmp;
        colliders.add(colliderCmp);
      }
    }
    return colliders;
  }

  private void handleTriggers() {
    // handle onTriggerEnter
    for (CollisionPair pair : activeCollisions) {
      if (!previousCollisions.contains(pair)) {
        triggerCollision(pair.e1, pair.e2, true);
        triggerCollision(pair.e2, pair.e1, true);
      }
    }
    // handle onTriggerLeave
    for (CollisionPair pair : previousCollisions) {
      if (!activeCollisions.contains(pair)) {
        triggerCollision(pair.e1, pair.e2, false);
        triggerCollision(pair.e2, pair.e1, false);
      }
    }
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
