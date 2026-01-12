package com.engine.inventory;

import com.engine.inventory.models.ItemDefinition;
import com.engine.inventory.logic.ItemRegistry;
import java.lang.reflect.Field;

public class TestUtils {
  public static void registerMockItem(String id, String name, int maxDurability, int price) {
    try {
      ItemDefinition def = new ItemDefinition();

      setPrivateField(def, "id", id);
      setPrivateField(def, "name", name);
      setPrivateField(def, "description", "Test Description");
      setPrivateField(def, "durability", maxDurability);
      setPrivateField(def, "price", price);

      ItemRegistry.register(id, def);
    } catch (Exception e) {
      throw new RuntimeException("Failed to create mock item via reflection", e);
    }
  }

  private static void setPrivateField(Object target, String fieldName, Object value) throws Exception {
    Field field = target.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.set(target, value);
  }
}
