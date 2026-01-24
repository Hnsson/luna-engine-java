package com.engine.inventory;

import com.engine.inventory.components.Inventory;
import com.engine.inventory.models.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LootManagerTest {

  private LootManager lootManager;
  private Inventory playerInv;
  private Inventory chestInv;

  @BeforeEach
  void setup() {
    lootManager = new LootManager();
    playerInv = new Inventory(5);
    chestInv = new Inventory(5);

    TestUtils.registerMockItem("wood", "Wood", 1, 10);
    TestUtils.registerMockItem("rock", "Rock", 1, 0);
  }

  @Test
  void testQuickLoot() {
    chestInv.setItem(new ItemStack("wood", 10), 0);

    // Loot index 0 from chest to player (quick loot)
    lootManager.loot(playerInv, chestInv, 0);

    assertNull(chestInv.getItem(0)); // Chest empty
    assertNotNull(playerInv.getItem(0)); // Player has it
    assertEquals(10, playerInv.getItem(0).getQuantity());
  }

  @Test
  void testLootSpecificSlotEmpty() {
    chestInv.setItem(new ItemStack("wood", 10), 0);

    // Loot to specific empty place
    lootManager.loot(playerInv, 2, chestInv, 0);

    assertNull(chestInv.getItem(0));
    assertNotNull(playerInv.getItem(2));
    assertEquals(10, playerInv.getItem(2).getQuantity());
  }

  @Test
  void testLootSwap() {
    chestInv.setItem(new ItemStack("wood", 10), 0);
    playerInv.setItem(new ItemStack("rock", 1), 0);

    // Drag Wood onto Rock (Swap)
    lootManager.loot(playerInv, 0, chestInv, 0);

    assertEquals("rock", chestInv.getItem(0).getItemId());
    assertEquals("wood", playerInv.getItem(0).getItemId());
  }

  @Test
  void testLootMerge() {
    chestInv.setItem(new ItemStack("wood", 5), 0);
    playerInv.setItem(new ItemStack("wood", 5), 0);

    // Drag Chest Wood onto Player Wood (Merge)
    lootManager.loot(playerInv, 0, chestInv, 0);

    assertNull(chestInv.getItem(0));
    assertEquals(10, playerInv.getItem(0).getQuantity());
  }

  @Test
  void testLootFullInventory() {
    // Fill player inventory
    for (int i = 0; i < 5; i++)
      playerInv.setItem(new ItemStack("rock", 1), i);

    chestInv.setItem(new ItemStack("wood", 10), 0);

    // Try quick loot
    lootManager.loot(playerInv, chestInv, 0);

    // Should fail, item stays in chest
    assertNotNull(chestInv.getItem(0));
    assertEquals("wood", chestInv.getItem(0).getItemId());
    assertEquals(10, chestInv.getItem(0).getQuantity());
  }
}
