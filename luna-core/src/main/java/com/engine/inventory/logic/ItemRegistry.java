package com.engine.inventory.logic;

import java.util.List;
import java.util.HashMap;
import java.util.Map;

import com.engine.inventory.io.ItemLoader;
import com.engine.inventory.models.ItemDefinition;
import com.engine.inventory.models.ItemStack;

public class ItemRegistry {
  private static final Map<String, ItemDefinition> items = new HashMap<>();

  public static void loadAllItems(String filePath) {
    List<ItemDefinition> loadedItems = ItemLoader.load(filePath);
    if (loadedItems == null) {
      System.err.println("[ERROR::ITEMREGISTRY] Could not load items from (" + filePath + ")");
      return;
    }

    for (ItemDefinition item : loadedItems) {
      register(item.getId(), item);
    }
  }

  public static void register(String itemId, ItemDefinition item) {
    items.put(itemId, item);
  }

  public static ItemDefinition getDefinition(String itemId) {
    return items.get(itemId);
  }

  public static ItemStack createStack(String itemId, int quantity) {
    if (!items.containsKey(itemId)) {
      System.err.println("Error: Item " + itemId + " does not exist.");
      return null;
    }

    return new ItemStack(itemId, quantity);
  }

}
