package com.engine.inventory.models;

import com.engine.inventory.logic.ItemRegistry;

// Will be the an actual inventory object based on an ItemDefinition
public class ItemStack {
  private final String itemId;

  private int quantity;
  private int maxQuantity = 999;
  private int currentDurability;
  private int stackPrice;

  public ItemStack(String itemId) {
    this.itemId = itemId;
    this.quantity = 1;
    this.currentDurability = ItemRegistry.getDefinition(itemId).getMaxDurability();
    this.stackPrice = ItemRegistry.getDefinition(itemId).getPrice() * this.quantity;
  }

  public ItemStack(String itemId, int quantity) {
    this.itemId = itemId;
    this.quantity = quantity;
    this.currentDurability = ItemRegistry.getDefinition(itemId).getMaxDurability();
    this.stackPrice = ItemRegistry.getDefinition(itemId).getPrice() * quantity;
  }

  public String getItemId() {
    return this.itemId;
  }

  public String getItemName() {
    return ItemRegistry.getDefinition(this.itemId).getName();
  }

  public String getItemDescription() {
    return ItemRegistry.getDefinition(this.itemId).getDescription();
  }

  public int getQuantity() {
    return this.quantity;
  }

  public void setQuantity(int amount) {
    if (amount < 0) {
      this.quantity = 0;
    } else if (amount > this.maxQuantity) {
      this.quantity = this.maxQuantity;
    } else {
      this.quantity = amount;
    }
  }

  public int increaseQuantity(int amount) {
    int spaceAvailable = this.maxQuantity - this.quantity;

    if (amount <= spaceAvailable) {
      this.quantity += amount;
      return 0; // 0 leftovers
    } else {
      this.quantity = this.maxQuantity;
      return amount - spaceAvailable; // return overflow
    }
  }

  public boolean reduceQuantity(int amount) {
    if ((this.quantity - amount) < 0) {
      this.quantity = 0;
    } else {
      this.quantity -= amount;
    }

    return this.quantity == 0;
  }

  public int getCurrentDurability() {
    return this.currentDurability;
  }

  public int getMaxDurability() {
    return ItemRegistry.getDefinition(this.itemId).getMaxDurability();
  }

  public int getStackPrice() {
    return this.stackPrice;
  }

  public int getItemPrice() {
    return ItemRegistry.getDefinition(this.itemId).getPrice();
  }

}
