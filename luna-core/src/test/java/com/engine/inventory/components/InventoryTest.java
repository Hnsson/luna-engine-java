package com.engine.inventory.components;

import com.engine.inventory.models.ItemStack;
import com.engine.inventory.TestUtils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class InventoryTest {

  private Inventory inventory;

  @BeforeEach
  void setup() {
    TestUtils.registerMockItem("sword", "Iron Sword", 100, 50);
    TestUtils.registerMockItem("stone", "Stone", 1, 1);

    inventory = new Inventory(10);
  }

  @Test
  void testAddItemEmptyInventory() {
    ItemStack stone = new ItemStack("stone", 5);
    boolean success = inventory.addItem(stone);

    assertTrue(success);
    assertNotNull(inventory.getItem(0));
    assertEquals("stone", inventory.getItem(0).getItemId());
  }

  @Test
  void testAddItemMerge() {
    inventory.addItem(new ItemStack("stone", 10));
    boolean success = inventory.addItem(new ItemStack("stone", 5));

    assertTrue(success);
    assertEquals(15, inventory.getItem(0).getQuantity());
    assertNull(inventory.getItem(1)); // Second slot should stay empty
  }

  @Test
  void testAddItemAtSpecificIndex() {
    ItemStack sword = new ItemStack("sword", 1);
    boolean success = inventory.addItem(sword, 5);

    assertTrue(success);
    assertNotNull(inventory.getItem(5));
    assertEquals("sword", inventory.getItem(5).getItemId());
  }

  @Test
  void testAddItemAtOccupiedIndexDifferentItem() {
    inventory.addItem(new ItemStack("stone", 1), 5);

    // Try adding sword to slot 5 (occupied by stone)
    boolean success = inventory.addItem(new ItemStack("sword", 1), 5);

    assertFalse(success);
    assertEquals("stone", inventory.getItem(5).getItemId());
  }

  @Test
  void testRetrieveItemSplitting() {
    inventory.addItem(new ItemStack("stone", 10));

    ItemStack retrieved = inventory.retrieveItem(0, 4);

    assertEquals(4, retrieved.getQuantity());
    assertEquals(6, inventory.getItem(0).getQuantity());
  }

  @Test
  void testRetrieveItemFullStack() {
    inventory.addItem(new ItemStack("stone", 10));

    ItemStack retrieved = inventory.retrieveItem(0, 10);

    assertEquals(10, retrieved.getQuantity());
    assertNull(inventory.getItem(0)); // Slot should be empty (null)
  }

  @Test
  void testInventoryFull() {
    // Fill inventory
    for (int i = 0; i < 10; i++) {
      inventory.addItem(new ItemStack("sword", 1), i);
    }

    // Try adding one more
    boolean success = inventory.addItem(new ItemStack("stone", 1));
    assertFalse(success);
  }
}
