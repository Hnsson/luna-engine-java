package com.engine.inventory.io;

import com.engine.inventory.models.ItemDefinition;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.Reader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.List;

public class ItemLoader {
  public static List<ItemDefinition> load(String path) {
    try {
      Reader reader = new InputStreamReader(ItemLoader.class.getResourceAsStream(path));
      Gson gson = new Gson();

      Type listType = new TypeToken<List<ItemDefinition>>() {
      }.getType();

      return gson.fromJson(reader, listType);

    } catch (Exception e) {
      System.err.println("Failed to load items from: " + path);
      e.printStackTrace();
      return null;
    }
  }
}
