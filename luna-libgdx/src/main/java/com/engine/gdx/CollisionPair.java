package com.engine.gdx;

import java.util.Objects;

import com.engine.ecs.Entity;

public class CollisionPair {
  public Entity e1;
  public Entity e2;

  public CollisionPair(Entity e1, Entity e2) {
    if (e1.getId() < e2.getId()) {
      this.e1 = e1;
      this.e2 = e2;
    } else {
      this.e1 = e2;
      this.e2 = e1;
    }
  }

  // Found this great article that described why this class wouldn't work without
  // fixing object equality
  // (https://medium.com/@AlexanderObregon/why-object-equality-in-java-isnt-always-what-you-expect-2c61278d9226)
  // Because like the articles says, with the HashSet (just like the
  // GDXCollisionSystem uses of collision pairs) Java calls the hashCode, checks,
  // then calls equals if something there which means we have to override those
  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if (obj == null || getClass() != obj.getClass())
      return false;
    CollisionPair other = (CollisionPair) obj;
    return e1.getId() == other.e1.getId() && e2.getId() == other.e2.getId();
  }

  // Make it unique Objects.hash but still say that the collision between (e1, e2)
  // is the same as (e2, e1) because sorting the id
  @Override
  public int hashCode() {
    int min = Math.min(e1.getId(), e2.getId());
    int max = Math.max(e1.getId(), e2.getId());

    return Objects.hash(min, max);
  }
}
