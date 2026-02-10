package com.engine.rendering.components;

import com.engine.ecs.Component;
import com.engine.ecs.Entity;

public class Camera extends Component {
  // Let camera system set this (transient)
  public transient Entity targetEntity;

  public float followSpeed;
  public float zoomSpeed;

  public float zoom;
  public float currentZoom;
  public boolean lockX, lockY;
  public float offsetX, offsetY;

  public Camera() {
    this(5.0f, 1.0f, 3.0f);
  }

  public Camera(float followSpeed, float zoom, float zoomSpeed) {
    this(followSpeed, zoom, zoomSpeed, false, false);
  }

  public Camera(float followSpeed, float zoom, float zoomSpeed, boolean lockX, boolean lockY) {
    this(followSpeed, zoom, zoomSpeed, lockX, lockY, 0f, 0f);
  }

  public Camera(float followSpeed, float zoom, float zoomSpeed, boolean lockX, boolean lockY, float offsetX,
      float offsetY) {
    this.followSpeed = followSpeed;
    this.zoom = zoom;
    this.currentZoom = zoom;
    this.zoomSpeed = zoomSpeed;
    this.lockX = lockX;
    this.lockY = lockY;
    this.offsetX = offsetX;
    this.offsetY = offsetY;
  }

  public void setTarget(Entity newTarget) {
    this.targetEntity = newTarget;
  }
}
