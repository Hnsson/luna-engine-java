package com.engine.ecs;

public abstract class Component {
  // Don't save entity to json file or else GSON will be stuck in infinite
  // serialization loop:
  // (Serialize entity -> serialize components (component has entity) ->
  // serialize entity -> ... --> CRASH)
  // Unfortunately have to be repopulated when loading in
  public transient Entity entity;

  public void init() {
  }

  public void eventHandler() {
  }

  public void update(float deltaTime) {
  }

  public void render() {
  }
}
