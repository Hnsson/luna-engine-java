package com.engine.rendering.components;

import com.engine.ecs.Component;
import com.engine.ecs.Entity;

public class Camera extends Component {
  // Let camera system set this (transient)
  public transient Entity targetEntity;

  public float followSpeed = 2f;
  public float zoom;
  public boolean lockX, lockY;
  public float offsetX, offsetY;

  public Camera() {
    this(5.0f, 1.0f);
  }

  public Camera(float followSpeed, float zoom) {
    this(followSpeed, zoom, false, false);
  }

  public Camera(float followSpeed, float zoom, boolean lockX, boolean lockY) {
    this(followSpeed, zoom, lockX, lockY, 0f, 0f);
  }

  public Camera(float followSpeed, float zoom, boolean lockX, boolean lockY, float offsetX, float offsetY) {
    this.followSpeed = followSpeed;
    this.zoom = zoom;
    this.lockX = lockX;
    this.lockY = lockY;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  public void setTarget(Entity newTarget) {
    this.targetEntity = newTarget;
  }
}
