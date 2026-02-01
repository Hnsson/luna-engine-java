package com.engine.gdx.scripts;

import com.engine.ecs.Entity;
import com.engine.gdx.Script;

public class PlayerTriggerScript extends Script {

  public PlayerTriggerScript() {
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
