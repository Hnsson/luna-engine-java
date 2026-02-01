package com.engine.gdx.scripts;

import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;
import com.engine.gdx.GDXScriptContext;
import com.engine.gdx.systems.GDXInputSystem;
import com.badlogic.gdx.Gdx;
import com.engine.Script;
import com.engine.SystemContext;

public class DialogueTriggerScript extends Script {
  private Entity currentTarget = null;

  public void update(float delta, SystemContext context) {
    GDXScriptContext gdxContext = (GDXScriptContext) context;
  }

  public void render(SystemContext context) {
    GDXScriptContext gdxContext = (GDXScriptContext) context;

    if (currentTarget != null) {
      Transform transform = currentTarget.getComponent(Transform.class);

      float textX = transform.position.x;
      float textY = transform.position.y + 30;

      gdxContext.renderer.beginUIFrame();
      gdxContext.renderer.drawText("Talk [E]", textX, textY, 1, 1, 1, 1);
      gdxContext.renderer.endUIFrame();
    }
  }

  public void eventHandler(SystemContext context) {
    GDXScriptContext gdxContext = (GDXScriptContext) context;

    if (Gdx.input.isKeyJustPressed(gdxContext.input.getKeybind(GDXInputSystem.Mapping.INTERACT))) {
      if (currentTarget != null) {
        if (!gdxContext.dialogue.isActive()) {
          gdxContext.dialogue.startDialogue(currentTarget, this.entity);
        }
      }
    }
  }

  @Override
  public void onTriggerEnter(Entity entity) {
    System.out.println("[onTriggerEnter]: with " + entity.getName());
    this.currentTarget = entity;
  }

  @Override
  public void onTriggerLeave(Entity entity) {
    System.out.println("[onTriggerLeave]: with " + entity.getName());
    this.currentTarget = null;
  }
}
