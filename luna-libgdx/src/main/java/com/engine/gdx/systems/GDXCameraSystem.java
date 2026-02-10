package com.engine.gdx.systems;

import com.engine.rendering.components.Camera;

import java.util.List;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.engine.GameSystem;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;

public class GDXCameraSystem implements GameSystem {
  private EntityManager entityManager;
  private OrthographicCamera gdxCamera;

  public GDXCameraSystem(EntityManager entityManager, OrthographicCamera gdxCamera) {
    this.entityManager = entityManager;
    this.gdxCamera = gdxCamera;
  }

  public void setTarget(Entity newTarget) {
    List<Entity> cameras = entityManager.getEntitiesWithAll(Camera.class, Transform.class);
    if (cameras.isEmpty()) {
      System.err.println("[CAMERASYSTEM::setTarget] No Camera Entity found in world!");
      return;
    }

    Entity camera = cameras.get(0);

    Camera camComp = camera.getComponent(Camera.class);
    camComp.targetEntity = newTarget;
  }

  @Override
  public void update(float delta) {
    List<Entity> cameras = entityManager.getEntitiesWithAll(Camera.class, Transform.class);
    if (cameras.isEmpty())
      return;

    Entity camera = cameras.get(0);

    Transform camTransform = camera.getComponent(Transform.class);
    Camera camComp = camera.getComponent(Camera.class);

    if (camComp.targetEntity != null) {
      Transform targetTransform = camComp.targetEntity.getComponent(Transform.class);
      if (targetTransform != null) {
        float destX = targetTransform.position.x + camComp.offsetX;
        float destY = targetTransform.position.y + camComp.offsetY;

        camTransform.position.x += (destX - camTransform.position.x) * camComp.followSpeed * delta;
        camTransform.position.y += (destY - camTransform.position.y) * camComp.followSpeed * delta;
      }
    }

    // Lerp zoom
    float zoomDiff = camComp.zoom - camComp.currentZoom;
    if (Math.abs(zoomDiff) > 0.001f) {
      camComp.currentZoom += zoomDiff * camComp.zoomSpeed * delta;
    } else {
      // Snap to finish if very close
      camComp.currentZoom = camComp.zoom;
    }

    gdxCamera.position.set(camTransform.position.x, camTransform.position.y, 0);
    gdxCamera.zoom = camComp.currentZoom;
    gdxCamera.update();
  }
}
