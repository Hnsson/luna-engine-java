package com.engine.ecs;

/*
 * The main goal with this Component is to keep the components
 * DATA-ONLY, that's why it doesn't have a render, update, and eventHandler
 * anymore. This is because a big optimization is to get rid of those functions
 * and have components data-only so that there can be a data class (component)
 * and then a "logic" class (the game implementation) that can bulk proccess
 * these components for better performance. Think this applies to what I'm doing:
 * (https://www.youtube.com/watch?v=WwkuAqObplU)
 */
public abstract class Component {
  /*
   * The entity uses transient because:
   * Don't save entity to json file or else GSON will be stuck in infinite
   * serialization loop:
   * (Serialize entity -> serialize components (component has entity) ->
   * serialize entity -> ... --> CRASH)
   * Unfortunately have to be repopulated when loading in (check
   * ECSSerializer.java)
   */
  public transient Entity entity;

  public void init() {
  }
}
