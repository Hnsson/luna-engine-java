package com.engine.gdx;

import com.engine.SystemContext;
import com.engine.ecs.EntityManager;
import com.engine.gdx.rendering.GDXRender;
import com.engine.gdx.systems.GDXDialogueSystem;
import com.engine.gdx.systems.GDXInputSystem;

public class GDXScriptContext implements SystemContext {
  public EntityManager entityManager;
  public GDXInputSystem input;
  public GDXDialogueSystem dialogue;
  public GDXRender renderer;

  public GDXScriptContext(EntityManager entityManager, GDXInputSystem input, GDXDialogueSystem dialogue,
      GDXRender renderer) {
    this.entityManager = entityManager;
    this.input = input;
    this.dialogue = dialogue;
    this.renderer = renderer;
  }

  @Override
  public EntityManager getEntityManager() {
    return entityManager;
  }
}
