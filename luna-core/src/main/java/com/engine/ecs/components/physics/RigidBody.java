package com.engine.ecs.components.physics;

import com.engine.ecs.Component;

public class RigidBody extends Component {
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
