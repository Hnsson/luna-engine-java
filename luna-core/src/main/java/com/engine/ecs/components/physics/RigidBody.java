package com.engine.ecs.components.physics;

import com.engine.ecs.Component;
import com.engine.math.Vector2f;

public class RigidBody extends Component {
  public Vector2f velocity = new Vector2f();

  public boolean useGravity;
  public float mass;
  public boolean immovable;

  public RigidBody() {
    this(false, 0f, false);
  }

  public RigidBody(boolean useGravity, float mass, boolean immovable) {
    this.useGravity = useGravity;
    this.mass = mass;
    this.immovable = false;
  }
}
