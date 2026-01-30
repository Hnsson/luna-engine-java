package com.engine.gdx.systems;

import java.util.Map;
import java.util.HashMap;
import java.util.List;

import com.engine.GameSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.logic.PlayerController;
import com.engine.rendering.components.SpriteRenderer;

public class GDXInputSystem implements GameSystem {
  private EntityManager entityManager;

  private Map<Mapping, Integer> inputMapping = new HashMap<>();

  public static enum Mapping {
    LEFT, RIGHT, UP, DOWN, PAUSE, SAVE, LOAD, ENTER, DEBUG
  };

  public GDXInputSystem(EntityManager entityManager) {
    this.entityManager = entityManager;

    inputMapping.put(Mapping.LEFT, Input.Keys.A);
    inputMapping.put(Mapping.RIGHT, Input.Keys.D);
    inputMapping.put(Mapping.UP, Input.Keys.W);
    inputMapping.put(Mapping.DOWN, Input.Keys.S);
    inputMapping.put(Mapping.PAUSE, Input.Keys.ESCAPE);
    inputMapping.put(Mapping.SAVE, Input.Keys.F5);
    inputMapping.put(Mapping.LOAD, Input.Keys.F8);
    inputMapping.put(Mapping.ENTER, Input.Keys.ENTER);
    inputMapping.put(Mapping.DEBUG, Input.Keys.F12);
  }

  public void changeKeybind(Mapping key, int value) {
    inputMapping.put(key, value);
  }

  public int getKeybind(Mapping key) {
    return inputMapping.get(key);
  }

  private void flipSprite(SpriteRenderer sprite, boolean flipX, boolean value) {
    if (sprite == null)
      return;

    if (flipX) {
      sprite.flipX = value;
    } else {
      sprite.flipY = value;
    }
  }

  @Override
  public void eventHandler() {
    List<Entity> entities = entityManager.getEntitiesWithAll(PlayerController.class, SpriteRenderer.class);

    for (Entity entity : entities) {
      PlayerController controller = entity.getComponent(PlayerController.class);
      SpriteRenderer sprite = entity.getComponent(SpriteRenderer.class);

      controller.moveDir.set(0, 0);

      if (Gdx.input.isKeyPressed(inputMapping.get(Mapping.LEFT))) {
        controller.moveDir.x = -1;
        flipSprite(sprite, true, true);
      }
      if (Gdx.input.isKeyPressed(inputMapping.get(Mapping.RIGHT))) {
        controller.moveDir.x = 1;
        flipSprite(sprite, true, false);
      }
      if (Gdx.input.isKeyPressed(inputMapping.get(Mapping.UP))) {
        controller.moveDir.y = 1;
      }
      if (Gdx.input.isKeyPressed(inputMapping.get(Mapping.DOWN))) {
        controller.moveDir.y = -1;
      }

      controller.moveDir.normalize();
    }
  }
}
