package com.engine.gdx.scripts.cpu;

import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.logic.CharacterController;
import com.engine.gdx.GDXScriptContext;
import com.engine.math.Vector2f;

import java.util.List;

import com.engine.Script;
import com.engine.SystemContext;

public class GDXNavigationScript extends Script {
  public enum NavMode {
    IDLE,
    FOLLOW_ENTITY,
    PATROL_CIRCUIT, // loops back to the beginning
    PATROL_REVERSAL // reverses direction at the end
  }

  public NavMode mode = NavMode.IDLE;
  public String targetEntityName = "";
  public List<Vector2f> waypoints;

  private float minDistance = 150.0f;

  private transient Entity targetEntity = null;
  private transient int currentWaypointIndex = 0;
  private transient int patrolReversalDirection = 1;
  private transient boolean isInitialized = false;

  private void initalize(GDXScriptContext gdxContext) {
    if (mode == NavMode.FOLLOW_ENTITY && !targetEntityName.isEmpty()) {
      for (Entity e : gdxContext.getEntityManager().getEntities()) {
        if (e.getName().equals(targetEntityName)) {
          this.targetEntity = e;
          break;
        }
      }
    }
    isInitialized = true;
  }

  private void handleFollowEntity() {
    CharacterController controller = this.entity.getComponent(CharacterController.class);
    if (targetEntity == null || controller == null)
      return;

    Transform transform = this.entity.getComponent(Transform.class);
    Transform targetTransform = targetEntity.getComponent(Transform.class);

    float deltaX = targetTransform.position.x - transform.position.x;
    float deltaY = targetTransform.position.y - transform.position.y;

    double distance = Math.hypot(deltaX, deltaY);
    if (distance <= minDistance) {
      stopMoving();
      return;
    }

    controller.moveDir.set(deltaX, deltaY);
  }

  private void handlePatrol() {
    // TODO: walk toward waypoints.get(currentWaypointIndex)
    // TODO: update waypoint index based on PATROL_CIRCUIT or PATROL_REVERSAL
  }

  private void stopMoving() {
    CharacterController controller = this.entity.getComponent(CharacterController.class);
    if (controller != null)
      controller.moveDir.set(0, 0);
  }

  public void update(float delta, SystemContext context) {
    GDXScriptContext gdxContext = (GDXScriptContext) context;

    if (!isInitialized) {
      initalize(gdxContext);
    }

    switch (mode) {
      case FOLLOW_ENTITY:
        handleFollowEntity();
        break;
      case PATROL_CIRCUIT:
      case PATROL_REVERSAL:
        handlePatrol();
        break;
      case IDLE:
      default:
        stopMoving();
        break;
    }
  }

  public void render(SystemContext context) {
    GDXScriptContext gdxContext = (GDXScriptContext) context;
  }

  public void eventHandler(SystemContext context) {
    GDXScriptContext gdxContext = (GDXScriptContext) context;
  }

  @Override
  public void onTriggerEnter(Entity entity) {
    System.out.println("[onTriggerEnter]: with " + entity.getName());

  }

  @Override
  public void onTriggerLeave(Entity entity) {
    System.out.println("[onTriggerLeave]: with " + entity.getName());

  }
}
