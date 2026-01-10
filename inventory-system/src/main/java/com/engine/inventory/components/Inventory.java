package com.engine.inventory.components;

import java.util.ArrayList;
import java.util.List;

import com.engine.ecs.Component;
import com.engine.inventory.logic.ItemRegistry;
import com.engine.inventory.models.ItemStack;

public class Inventory extends Component {
  private List<ItemStack> inventory;
  private final int capacity;

  public Inventory() {
    this.capacity = 24;
    this.inventory = new ArrayList<>(this.capacity);

    for (int i = 0; i < this.capacity; i++) {
      this.inventory.add(null); // so the getting works on every index
    }
  }

  public Inventory(int capacity) {
    this.capacity = capacity;
    this.inventory = new ArrayList<>(capacity);

    for (int i = 0; i < capacity; i++) {
      this.inventory.add(null); // ---
    }
  }

  public boolean addItem(ItemStack item) {
    for (int i = 0; i < capacity; i++) {
      ItemStack slot = inventory.get(i);
      if (slot != null && slot.getItemId().equals(item.getItemId())) {
        // Same item already exists, try to fit quantity
        int overflow = slot.increaseQuantity(item.getQuantity());

        item.setQuantity(overflow);

        if (overflow == 0) {
          cleanup(); // just to fix ghost items
          return true;
        }
      }
    }
    // If items left then just find first empty slot
    if (item.getQuantity() > 0) {
      for (int i = 0; i < capacity; i++) {
        if (inventory.get(i) == null) {
          inventory.set(i, item);
          return true;
        }
      }
    }
    return false; // Inventory is full, items left
  }

  public boolean addItem(ItemStack item, int index) {
    if (index < 0 || index >= capacity)
      return false;

    ItemStack slot = inventory.get(index);
    if (slot == null) {
      inventory.set(index, item);
      return true;
    } else if (slot.getItemId().equals(item.getItemId())) {
      // If item is same, the try to merge quantity
      int overflow = slot.increaseQuantity(item.getQuantity());
      item.setQuantity(overflow);

      cleanup(); // just to fix ghost items
      return overflow == 0;
    } else {
      return false; // Cannot add item on another item (create swap function later)
    }
  }

  // Noticed ghost items in inventory, maybe this is not a good solution
  private void cleanup() {
    for (int i = 0; i < capacity; i++) {
      ItemStack stack = inventory.get(i);
      if (stack != null && stack.getQuantity() <= 0) {
        inventory.set(i, null);
      }
    }
  }

  public void deleteItem(int index) {
    if (index >= 0 && index < capacity) {
      inventory.set(index, null);
    }
  }

  public ItemStack retrieveItem(int index) {
    if (index < 0 || index >= capacity)
      return null;

    ItemStack item = inventory.get(index);

    if (item != null) {
      inventory.set(index, null);
    }

    return item;
  }

  public ItemStack retrieveItem(int index, int amount) {
    ItemStack currentStack = inventory.get(index);
    if (currentStack == null)
      return null;

    if (currentStack.getQuantity() <= amount) {
      inventory.set(index, null);
      return currentStack; // just return all
    } else {
      currentStack.reduceQuantity(amount);

      // Return new stack with the amount
      ItemStack splitStack = ItemRegistry.createStack(currentStack.getItemId(), amount);
      return splitStack;
    }
  }

  public List<ItemStack> getInventory() {
    return this.inventory;
  }

  @Override
  public void init() {
  }

  @Override
  public void update() {
  }

  @Override
  public void render() {
  }
}
