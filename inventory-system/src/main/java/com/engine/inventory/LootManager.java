package com.engine.inventory;

import com.engine.inventory.components.Inventory;
import com.engine.inventory.models.ItemStack;

public class LootManager {
  /**
   * Attemps to move an item from one inventory to another (quick loot)
   *
   * Attemps to move item in 'source' inventory at index 'source_index' to
   * 'destination' Inventory
   */
  public void loot(Inventory destination, Inventory source, int sourceIndex) {
    ItemStack sourceItem = source.getItem(sourceIndex);
    // You can't take empty item.
    if (sourceItem == null)
      return;

    sourceItem = source.retrieveItem(sourceIndex);

    boolean added = destination.addItem(sourceItem);
    if (!added) {
      // Destination inventory full, add it back to source (fast)
      source.setItem(sourceItem, sourceIndex);
    }
  }

  /**
   * Attemps to move an item from one inventory to another in a specific place
   *
   * Attemps to move item in 'source' inventory at index 'source_index' to
   * 'destination' Inventory at index 'destinationIndex'
   */
  public void loot(Inventory destination, int destinationIndex, Inventory source, int sourceIndex) {
    ItemStack sourceItem = source.getItem(sourceIndex);
    ItemStack destinationItem = destination.getItem(destinationIndex);
    // You can't take empty item.
    if (sourceItem == null)
      return;
    // If destination has an item and its different, then swap
    if (destinationItem != null && !destinationItem.getItemId().equals(sourceItem.getItemId())) {
      sourceItem = source.retrieveItem(sourceIndex);
      destinationItem = destination.retrieveItem(destinationIndex);

      destination.setItem(sourceItem, destinationIndex);
      source.setItem(destinationItem, sourceIndex);
      return;
    }
    // If destination has an item and its the same or no item, then add (and
    // possibly merge)
    sourceItem = source.retrieveItem(sourceIndex);

    boolean added = destination.addItem(sourceItem, destinationIndex);
    if (!added) {
      // Either full inventory or exceeding stack max capacity
      // return item to source with exceeding quantity
      source.setItem(sourceItem, sourceIndex);
    }
  }
}
