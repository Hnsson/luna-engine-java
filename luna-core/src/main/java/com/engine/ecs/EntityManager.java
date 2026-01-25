package com.engine.ecs;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.engine.FileContext;

/*
 * Will manage the storage of all entities.
 * It prevents the stale-references issue on the systems when
 * entities are loaded / reloaded, for example when you loadGame()
 * at run-time, so that systems using that old references wont go bad.
 * Also allowed for some cool searching functionality for Entities based
 * on components
 */
public class EntityManager {
  private List<Entity> entities = new ArrayList<>();
  private Map<Class<? extends Component>, List<Entity>> componentEntityMap = new HashMap<>();

  public EntityManager() {
  }

  public void loadEntities(String path, FileContext fileContext, ECSSerializer serializer, boolean replace) {
    /*
     * Replace boolean is to determine if I clear and replace all entities or not,
     * need this case because maybe I want to replace all if the player died (reset
     * level) And maybe I don't want to replace all if I load in a level or like a
     * wave of enemies but don't want to reset the player and every other entitiy.
     */
    if (replace)
      clear();

    String jsonContent = fileContext.readFile(path);
    List<Entity> loadedEntities = serializer.loadEntities(jsonContent);

    if (loadedEntities != null) {
      for (Entity entity : loadedEntities) {
        addEntity(entity);
      }
    }
  }

  public void saveEntities(String path, FileContext fileContext, ECSSerializer serializer) {
    String jsonContent = serializer.saveEntities(entities);

    fileContext.writeFile(path, jsonContent);
  }

  public void addEntity(Entity entity) {
    entities.add(entity);

    for (Component component : entity.getComponents()) {
      // safety, creates list if empty
      componentEntityMap
          .computeIfAbsent(component.getClass(), k -> new ArrayList<>())
          .add(entity);
    }
  }

  public void removeEntity(Entity entity) {
    if (entity == null)
      return;

    entities.remove(entity);

    for (Component component : entity.getComponents()) {
      Class<? extends Component> type = component.getClass();

      List<Entity> list = componentEntityMap.get(type);

      if (list != null)
        list.remove(entity);
    }
  }

  public List<Entity> getEntities() {
    return entities;
  }

  public Entity getEntity(int index) {
    return entities.get(index);
  }

  public List<Entity> getEntitiesWith(Class<? extends Component> componentClass) {
    return componentEntityMap.get(componentClass);
  }

  // Implemented using a set so that it is OR. Like if entity got <Component A> or
  // <Component B> when called (Component A.class, Component B.class)
  public List<Entity> getEntitiesWith(Class<? extends Component>... componentClasses) {
    Set<Entity> entities = new HashSet<>();

    for (Class<? extends Component> classType : componentClasses) {
      entities.addAll(componentEntityMap.getOrDefault(classType, Collections.emptyList()));
    }

    return new ArrayList<>(entities);
  }

  public void clear() {
    entities.clear();
    componentEntityMap.clear();
  }
}
