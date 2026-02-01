package com.engine.ecs;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.engine.FileContext;
import com.engine.Script;

/*
 * Will manage the storage of all entities.
 * It prevents the stale-references issue on the systems when
 * entities are loaded / reloaded, for example when you loadGame()
 * at run-time, so that systems using that old references wont go bad.
 * Also allowed for some cool searching functionality for Entities based
 * on components
 */
public class EntityManager {
  // Add a list for the script system to more easily use because they are quite
  // separate
  private List<Entity> entitiesWithScripts = new ArrayList<>();
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

  public Entity loadEntity(String path, FileContext fileContext, ECSSerializer serializer, boolean replace) {
    if (replace)
      clear();

    String jsonContent = fileContext.readFile(path);
    Entity loadedEntity = serializer.loadEntity(jsonContent);

    if (loadedEntity != null) {
      addEntity(loadedEntity);
    }

    return loadedEntity;
  }

  public void saveEntity(String path, Entity entity, FileContext fileContext, ECSSerializer serializer) {
    String jsonContent = serializer.saveEntity(entity);

    fileContext.writeFile(path, jsonContent);
  }

  public void saveEntities(String path, FileContext fileContext, ECSSerializer serializer, Entity... excludeEntities) {
    // Exclude entities from being saved (because player is saved in separate file)
    List<Entity> filteredEntities = new ArrayList<>(entities);
    if (excludeEntities != null && excludeEntities.length > 0) {
      filteredEntities.removeAll(Arrays.asList(excludeEntities));
    }

    String jsonContent = serializer.saveEntities(filteredEntities);
    fileContext.writeFile(path, jsonContent);
  }

  public void addEntity(Entity entity) {
    entities.add(entity);
    boolean hasScript = false;

    for (Component component : entity.getComponents()) {
      // safety, creates list if empty
      componentEntityMap
          .computeIfAbsent(component.getClass(), k -> new ArrayList<>())
          .add(entity);
      if (component instanceof Script)
        hasScript = true;
    }

    if (hasScript)
      entitiesWithScripts.add(entity);
  }

  public void removeEntity(Entity entity) {
    if (entity == null)
      return;

    entities.remove(entity);
    entitiesWithScripts.remove(entity);

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

  public List<Entity> getEntitiesWithScripts() {
    return entitiesWithScripts;
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

  // Implemented using a set so that it is AND. Like if entity got <Component A>
  // and <Component B> when called (Component A.class, Component B.class)
  public List<Entity> getEntitiesWithAll(Class<? extends Component>... componentClasses) {
    if (componentClasses.length == 0) {
      return new ArrayList<>();
    }
    // get all entities that has first component
    Set<Entity> result = new HashSet<>(
        componentEntityMap.getOrDefault(componentClasses[0], Collections.emptyList()));

    for (int i = 1; i < componentClasses.length; i++) {
      // continuously check the next component
      List<Entity> nextBatch = componentEntityMap.getOrDefault(componentClasses[i], Collections.emptyList());
      // Remove those who had first component but not second, third, ...
      result.retainAll(nextBatch);
    }

    return new ArrayList<>(result);
  }

  public void clear() {
    entities.clear();
    componentEntityMap.clear();
  }
}
