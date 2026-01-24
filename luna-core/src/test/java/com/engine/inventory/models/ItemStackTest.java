package com.engine.inventory.models;

import org.junit.jupiter.api.Test;

import com.engine.inventory.TestUtils;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;

class ItemStackTest {

  @BeforeAll
  static void setupRegistry() {
    TestUtils.registerMockItem("potion", "Health Potion", 1, 10);
  }

  @Test
  void testInitialization() {
    ItemStack stack = new ItemStack("potion", 5);
    assertEquals("potion", stack.getItemId());
    assertEquals(5, stack.getQuantity());
    assertEquals(50, stack.getStackPrice());
  }

  @Test
  void testIncreaseQuantityNormal() {
    ItemStack stack = new ItemStack("potion", 50);
    int overflow = stack.increaseQuantity(100);

    assertEquals(150, stack.getQuantity());
    assertEquals(0, overflow);
  }

  @Test
  void testIncreaseQuantityOverflow() {
    ItemStack stack = new ItemStack("potion", 990);
    // Max is 999. Adding 20 should make it 999 and return 11 overflow
    int overflow = stack.increaseQuantity(20);

    assertEquals(999, stack.getQuantity());
    assertEquals(11, overflow);
  }

  @Test
  void testReduceQuantity() {
    ItemStack stack = new ItemStack("potion", 10);
    boolean isEmpty = stack.reduceQuantity(4);

    assertEquals(6, stack.getQuantity());
    assertFalse(isEmpty);
  }

  @Test
  void testReduceQuantityToZero() {
    ItemStack stack = new ItemStack("potion", 5);
    boolean isEmpty = stack.reduceQuantity(5);

    assertEquals(0, stack.getQuantity());
    assertTrue(isEmpty);
  }

  @Test
  void testReduceQuantityBelowZero() {
    ItemStack stack = new ItemStack("potion", 5);
    boolean isEmpty = stack.reduceQuantity(10);

    assertEquals(0, stack.getQuantity());
    assertTrue(isEmpty);
  }
}
