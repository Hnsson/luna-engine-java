package com.engine.inventory;

import com.engine.inventory.components.Inventory;
import com.engine.inventory.models.ItemStack;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TradeManagerTest {

  private TradeManager tradeManager;
  private Inventory initiator;
  private Inventory counterParty;

  @BeforeEach
  void setup() {
    tradeManager = new TradeManager();
    initiator = new Inventory(5);
    counterParty = new Inventory(5);

    TestUtils.registerMockItem("wood", "Wood", 1, 10);
    TestUtils.registerMockItem("rock", "Rock", 1, 5);
  }

  /*
   * Test a successful trade: initiator provides (offer) more or equal to the
   * value to the counterparty.
   *
   * 1 Wood (10g) for 1 Rock (5g)
   */
  @Test
  void testTradeItemsSuccessful() {
    initiator.setItem(new ItemStack("wood", 1), 0);
    counterParty.setItem(new ItemStack("rock", 1), 0);

    boolean result = tradeManager.tradeItems(initiator, 0, 1, counterParty, 0, 1);
    assertTrue(result, "Trade should succeed when initator offers mroe value");

    // has?
    assertTrue(initiator.hasItem(new ItemStack("rock", 1)));
    assertTrue(counterParty.hasItem(new ItemStack("wood", 1)));

    // right pos?
    assertEquals("rock", initiator.getItem(0).getItemId(), "Initiator should have received Rock");
    assertEquals("wood", counterParty.getItem(0).getItemId(), "Counterparty should have received Wood");
  }

  /*
   * Test a failed trade: initator provides (offer) lesser value than the
   * counterparty
   *
   * 1 Rock (5g) for 1 Wood (10g)
   */
  @Test
  void testTradeItemsFailed() {
    initiator.setItem(new ItemStack("rock", 1), 0);
    counterParty.setItem(new ItemStack("wood", 1), 0);

    boolean result = tradeManager.tradeItems(initiator, 0, 1, counterParty, 0, 1);

    assertFalse(result, "Trade should fail when initiator offers less value");

    // no items moved
    assertEquals("rock", initiator.getItem(0).getItemId());
    assertEquals("wood", counterParty.getItem(0).getItemId());
  }

  /*
   * Test trading partial items in a stack's quantity.
   *
   * 1/10 Wood (10g) for 1 Rock (5g)
   */
  @Test
  void testTradePartialItems() {
    initiator.setItem(new ItemStack("wood", 10), 0);
    counterParty.setItem(new ItemStack("rock", 1), 0);

    boolean result = tradeManager.tradeItems(initiator, 0, 1, counterParty, 0, 1);

    assertTrue(result);

    assertEquals(9, initiator.getItem(0).getQuantity(), "Initiator should have 9 wood after trade");
    assertEquals("rock", initiator.getItem(1).getItemId(), "Initiator recieved rock");

    assertEquals("wood", counterParty.getItem(0).getItemId(), "Couterparty recieved wood");
    assertEquals(1, counterParty.getItem(0).getQuantity());
  }

  /*
   * Successfully trade item for gold (currency)
   *
   * 1 Wood (10g) for 10g
   */
  @Test
  void testTradeItemsForGoldSuccessful() {
    initiator.setItem(new ItemStack("wood", 2), 0);
    counterParty.addCurrency(100);

    // Trade same value: 1 Wood (10g) for 10g
    boolean result1 = tradeManager.tradeItemForGold(initiator, 0, 1, counterParty, 10);
    assertTrue(result1);
    assertEquals(10, initiator.getCurrency(), "Initiator gains 10 gold");
    assertEquals(90, counterParty.getCurrency(), "Counterparty spends 10 gold");

    assertEquals(1, initiator.getItem(0).getQuantity(), "Initiator sold 1 wood");
    assertEquals("wood", counterParty.getItem(0).getItemId(), "Counterparty recieved wood");
    assertEquals(1, counterParty.getItem(0).getQuantity(), "Counterparty recieved 1 wood");

    // Trade with initiator disadvantage: 1 Wood (10g) for 5g
    boolean result2 = tradeManager.tradeItemForGold(initiator, 0, 1, counterParty, 5);
    assertTrue(result2);
    assertEquals(15, initiator.getCurrency(), "Initiator gains 5 gold");
    assertEquals(85, counterParty.getCurrency(), "Counterparty spends 5 gold");

    assertNull(initiator.getItem(0), "Initiator gave all of his wood");
    assertEquals(2, counterParty.getItem(0).getQuantity(), "Counterparty recieved 1 wood (2 total)");
  }

  /*
   * Test a failed trade: initator provides (offer) and want counterparty
   * to provide more gold than the offer is worth
   *
   * 1 Wood (10g) for 15g
   */
  @Test
  void testTradeItemsForGoldFailed() {
    initiator.setItem(new ItemStack("wood", 1), 0);
    counterParty.addCurrency(5);

    boolean result = tradeManager.tradeItemForGold(initiator, 0, 1, counterParty, 15);

    assertFalse(result, "Counterparty wont agree to that");
    assertEquals(0, initiator.getCurrency()); // no change
    assertEquals(5, counterParty.getCurrency()); // no change
    assertEquals("wood", initiator.getItem(0).getItemId()); // no change
    assertEquals(1, initiator.getItem(0).getQuantity()); // no change
  }

  /*
   * Successfully buy items with gold (currency)
   *
   * With 10g buy 1 Wood (10g)
   */
  @Test
  void testTradeGoldForItemSuccessful() {
    counterParty.setItem(new ItemStack("wood", 1), 0);
    initiator.addCurrency(50);

    boolean result = tradeManager.tradeGoldForItem(initiator, 10, counterParty, 0, 1);

    assertTrue(result);
    assertEquals(40, initiator.getCurrency(), "Initiator spent 10 gold");
    assertEquals(10, counterParty.getCurrency(), "Counterparty gained 10 gold");
    assertEquals("wood", initiator.getItem(0).getItemId(), "Initiator received item");
    assertNull(counterParty.getItem(0));
  }

  /*
   * Failed to buy: Initiator provided less currency than counterparties
   * requests' worth
   *
   * Buy 1 Wood (10g) with 5 gold (currency)
   */
  @Test
  void testTradeGoldForItemFailed() {
    counterParty.setItem(new ItemStack("wood", 1), 0);
    initiator.addCurrency(100);

    boolean result = tradeManager.tradeGoldForItem(initiator, 5, counterParty, 0, 1);

    assertFalse(result, "Offer (5g) is less than value (10g)");
    assertEquals(100, initiator.getCurrency());
    assertEquals(1, counterParty.getItem(0).getQuantity(), "Counterparty still has the items");
  }

  /*
   * Successfully trade gold (currency) for gold (currency)
   *
   * With initiators disadvantage in mind
   */
  @Test
  void testTradeGoldForGoldSuccessful() {
    initiator.addCurrency(100);
    counterParty.addCurrency(50);

    // Trade same value 10 gold (currency) for 10 gold (currency) = unchanged but
    // still allowed
    boolean result1 = tradeManager.tradeGoldForGold(initiator, 10, counterParty, 10);
    assertTrue(result1);
    assertEquals(100, initiator.getCurrency());
    assertEquals(50, counterParty.getCurrency());

    // Trade with initiator disadvantage: 90 gold (currency) for 60 gold (currency)
    boolean result2 = tradeManager.tradeGoldForGold(initiator, 100, counterParty, 50);
    assertTrue(result2);
    assertEquals(50, initiator.getCurrency());
    assertEquals(100, counterParty.getCurrency());
  }

  /*
   * Failed trade: Initator offers less currency than counterparties offer
   *
   * Trade 10 gold (currency) for 15 gold (currency)
   */
  @Test
  void testTradeGoldForGoldFailed() {
    initiator.addCurrency(100);
    counterParty.addCurrency(100);

    boolean result = tradeManager.tradeGoldForGold(initiator, 10, counterParty, 15);

    assertFalse(result);
    assertEquals(100, initiator.getCurrency()); // no change
    assertEquals(100, counterParty.getCurrency()); // no change
  }

  /*
   * Edge case, trade with empty slot
   *
   * Trade null for Item/currency
   */
  @Test
  void testTradeEdgeCases() {
    counterParty.addCurrency(10);
    counterParty.setItem(new ItemStack("wood", 1), 0);

    boolean result1 = tradeManager.tradeItems(initiator, 0, 1, counterParty, 0, 1);
    assertFalse(result1);
    assertEquals("wood", counterParty.getItem(0).getItemId()); // no change
    assertEquals(1, counterParty.getItem(0).getQuantity()); // no change

    boolean result2 = tradeManager.tradeItemForGold(initiator, 0, 1, counterParty, 10);
    assertFalse(result2);
    assertEquals(10, counterParty.getCurrency());
  }
}
