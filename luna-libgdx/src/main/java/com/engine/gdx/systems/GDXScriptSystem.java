package com.engine.gdx.systems;

import java.util.List;

import com.engine.GameSystem;
import com.engine.ecs.Component;
import com.engine.ecs.Entity;
import com.engine.gdx.GDXScriptContext;
import com.engine.Script;

public class GDXScriptSystem implements GameSystem {
  private GDXScriptContext context;

  public GDXScriptSystem(GDXScriptContext context) {
    this.context = context;
  }

  @Override
  public void update(float delta) {
    List<Entity> entities = context.getEntityManager().getEntitiesWithScripts();

    // Feels dumb doing this every frame
    for (Entity e : entities) {
      for (Component cmp : e.getComponents()) {
        if (cmp instanceof Script) {
          Script script = (Script) cmp;
          script.update(delta, context);
        }
      }
    }
  }

  @Override
  public void eventHandler() {
    List<Entity> entities = context.getEntityManager().getEntitiesWithScripts();

    for (Entity e : entities) {
      for (Component cmp : e.getComponents()) {
        if (cmp instanceof Script) {
          Script script = (Script) cmp;
          script.eventHandler(context);
        }
      }
    }
  }

  @Override
  public void render() {
    List<Entity> entities = context.getEntityManager().getEntitiesWithScripts();

    for (Entity e : entities) {
      for (Component cmp : e.getComponents()) {
        if (cmp instanceof Script) {
          Script script = (Script) cmp;
          script.render(context);
        }
      }
    }
  }
}
