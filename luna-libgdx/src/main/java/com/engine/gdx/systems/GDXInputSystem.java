package com.engine.gdx.systems;

import java.util.Map;
import java.util.HashMap;

import com.engine.GameSystem;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.engine.ecs.components.logic.PlayerController;
import com.engine.rendering.components.SpriteRenderer;

public class GDXInputSystem implements GameSystem {
  private Map<Mapping, Integer> inputMapping = new HashMap<>();
  private PlayerController controller;
  private SpriteRenderer sprite;

  public static enum Mapping {
    LEFT, RIGHT, UP, DOWN, PAUSE, SAVE, LOAD
  };

  public GDXInputSystem(PlayerController controller) {
    this(controller, null);
  }

  public GDXInputSystem(PlayerController controller, SpriteRenderer sprite) {
    this.controller = controller;
    this.sprite = sprite;

    inputMapping.put(Mapping.LEFT, Input.Keys.A);
    inputMapping.put(Mapping.RIGHT, Input.Keys.D);
    inputMapping.put(Mapping.UP, Input.Keys.W);
    inputMapping.put(Mapping.DOWN, Input.Keys.S);
    inputMapping.put(Mapping.PAUSE, Input.Keys.ESCAPE);
    inputMapping.put(Mapping.SAVE, Input.Keys.F5);
    inputMapping.put(Mapping.LOAD, Input.Keys.F8);
  }

  public void changeKeybind(Mapping key, int value) {
    inputMapping.put(key, value);
  }

  public int getKeybind(Mapping key) {
    return inputMapping.get(key);
  }

  private void flipSprite(boolean flipX, boolean value) {
    if (sprite == null)
      return;

    if (flipX) {
      sprite.flipX = value;
    } else {
      sprite.flipY = value;
    }
  }

  public void setTarget(PlayerController controller, SpriteRenderer sprite) {
    this.controller = controller;
    this.sprite = sprite;
  }

  @Override
  public void eventHandler() {
    if (controller == null)
      return;

    controller.moveDir.set(0, 0);

    if (Gdx.input.isKeyPressed(inputMapping.get(Mapping.LEFT))) {
      controller.moveDir.x = -1;
      flipSprite(true, true);
    }
    if (Gdx.input.isKeyPressed(inputMapping.get(Mapping.RIGHT))) {
      controller.moveDir.x = 1;
      flipSprite(true, false);
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
