package com.engine.ecs;

import com.engine.level.LevelData;
import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.Collections;
import java.util.List;

/*
 * Wanted to use the same graph deserialization with GSON that I used for dialogue on the actual entities
 * and components so I can easily save and load the game. The GsonBuilder was used to register
 * the type adapter for the components.
 * Didn't like how the RuntimeTypeAdapterFactory had to register all subtypes (components):
 * (https://futurestud.io/tutorials/how-to-deserialize-a-list-of-polymorphic-objects-with-gson)
 * final RuntimeTypeAdapterFactory<AbstractContainer> typeFactory = RuntimeTypeAdapterFactory  
        .of(Animal.class, "type") // Here you specify which is the parent class and what field particularizes the child class.
        .registerSubtype(Dog.class, "dog") // if the flag equals the class name, you can skip the second parameter. This is only necessary, when the "type" field does not equal the class name.
        .registerSubtype(Cat.class, "cat");
 *
 * So just went with a custom TypeAdapter and created an adapter that uses (Class.forName)
 * to dynamically load component classes based on the stored "type" string.
 * So I don't have to be update it every time a new component is created.
 *
 */
public class ECSSerializer {

  private Gson mainGson; // writes the polymorphic with the custom adapter
  private Gson workerGson; // writes just data (no adapter)

  public ECSSerializer() {
    this.workerGson = new GsonBuilder()
        .setPrettyPrinting()
        .create();

    GsonBuilder builder = new GsonBuilder();
    builder.setPrettyPrinting();
    builder.registerTypeAdapter(Component.class, new ComponentAdapter(workerGson));

    this.mainGson = builder.create();
  }

  public LevelData loadLevel(String json) {
    if (json == null || json.isEmpty())
      return null;

    LevelData level = mainGson.fromJson(json, LevelData.class);

    if (level != null && level.entities != null) {
      for (Entity entity : level.entities) {
        entity.init();
      }
      return level;
    }
    return null;
  }

  /*
   * Saves a singular entity, like if player need a separate file
   */
  public String saveEntity(Entity entity) {
    if (entity == null)
      return null;
    return mainGson.toJson(entity);
  }

  /*
   * Saves all entities in a list of entities
   */
  public String saveEntities(List<Entity> entities) {
    // Just return empty json if no entities to save
    if (entities == null || entities.isEmpty())
      return "[]";

    Type listType = new TypeToken<List<Entity>>() {
    }.getType();
    return mainGson.toJson(entities, listType);
  }

  /*
   * Load a singular entity
   */
  public Entity loadEntity(String json) {
    if (json == null || json.isEmpty())
      return null;
    // Now that we removed (added transient to entity component) we need to reapply
    // it's owner to all components (is there a better way?)
    Entity loadedEntity = mainGson.fromJson(json, Entity.class);

    if (loadedEntity.getComponents() != null) {
      loadedEntity.init();
    }
    return loadedEntity;
  }

  /*
   * Loads all entities from JSON into a list of entities
   */
  public List<Entity> loadEntities(String json) {
    if (json == null || json.isEmpty())
      return Collections.emptyList();

    Type listType = new TypeToken<List<Entity>>() {
    }.getType();
    List<Entity> loadedEntities = mainGson.fromJson(json, listType);

    // Now that we removed (added transient to entity component) we need to reapply
    // it's owner to all components (is there a better way?)
    if (loadedEntities != null) {
      for (Entity entity : loadedEntities) {
        entity.init();
      }

      return loadedEntities;
    }

    return Collections.emptyList();
  }

  private static class ComponentAdapter implements JsonSerializer<Component>, JsonDeserializer<Component> {
    private Gson worker;

    public ComponentAdapter(Gson worker) {
      this.worker = worker;
    }

    @Override
    public JsonElement serialize(Component cmp, Type typeOfCmp, JsonSerializationContext context) {
      JsonObject result = new JsonObject();

      result.addProperty("type", cmp.getClass().getName());
      result.add("data", worker.toJsonTree(cmp));

      return result;
    }

    @Override
    public Component deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
        throws JsonParseException {

      JsonObject jsonObject = json.getAsJsonObject();
      String typeName = jsonObject.get("type").getAsString();
      JsonElement data = jsonObject.get("data");

      try {
        // So instead of registering just cast it to the stored class type
        Class<?> cls = Class.forName(typeName);

        return (Component) worker.fromJson(data, cls);

      } catch (ClassNotFoundException e) {
        throw new JsonParseException("Unknown component type: " + typeName);
      }
    }
  }
}
