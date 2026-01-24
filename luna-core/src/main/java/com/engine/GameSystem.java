package com.engine;

/*
 * I want all systems (render, movement, ...) to follow this interface
 * guideline so in the actual game they can be easily "bulk" updated,
 * rendered, eventHandler. This will create a clean design in the
 * main game application.
 *
 * This will be the "logic" / "puppet master" class mentioned in (check ecs/Component.java)
 */
public interface GameSystem {
  default void eventHandler() {
  }

  default void update(float deltaTime) {
  }

  default void render() {
  }
}
