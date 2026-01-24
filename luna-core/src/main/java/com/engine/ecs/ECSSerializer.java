package com.engine.ecs;

import com.google.gson.*;
import java.lang.reflect.Type;

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

  // It starts serializing entity and when it hit the component list inside the
  // entity, the serializer function will be used on it
  public String saveEntity(Entity entity) {
    return mainGson.toJson(entity);
  }

  public Entity loadEntity(String json) {
    // Now that we removed (added transient to entity component) we need to reapply
    // it's owner to all components (is there a better way?)
    Entity loadedEntity = mainGson.fromJson(json, Entity.class);

    if (loadedEntity.getComponents() != null) {
      for (Component cmp : loadedEntity.getComponents()) {
        cmp.entity = loadedEntity;
      }
    }
    return loadedEntity;
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
