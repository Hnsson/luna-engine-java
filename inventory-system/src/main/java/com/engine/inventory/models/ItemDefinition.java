package com.engine.inventory.models;

// Will be the singular unchanged definition of an item
public class ItemDefinition {
  private String id;
  private String name;
  private String description;
  private int durability;
  private int price;

  public String getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDescription() {
    return this.description;
  }

  public int getMaxDurability() {
    return this.durability;
  }

  public int getPrice() {
    return this.price;
  }
}
