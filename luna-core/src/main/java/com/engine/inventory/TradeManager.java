package com.engine.inventory;

import com.engine.inventory.components.Inventory;
import com.engine.inventory.models.ItemStack;

public class TradeManager {
  /*
   * The initiator always has the disadvantage.
   *
   * The counterparty will always only accept trades that are equal
   * to or less to what is given by the initiator's offer.
   * Trade offers can be done 4-ways: item-to-item, gold-to-item,
   * item-to-gold, gold-to-gold.
   */

  public boolean tradeItems(Inventory initiator, int initIndex, int initAmount, Inventory counterparty, int countIndex,
      int countAmount) {
    ItemStack offer = initiator.getItem(initIndex);
    ItemStack request = counterparty.getItem(countIndex);

    if (offer == null || request == null)
      return false;

    int offerValue = offer.getItemPrice() * initAmount;
    int requestValue = request.getItemPrice() * countAmount;

    if (offerValue >= requestValue) {
      ItemStack given = initiator.retrieveItem(initIndex, initAmount);
      ItemStack received = counterparty.retrieveItem(countIndex, countAmount);

      if (counterparty.addItem(given)) {
        if (initiator.addItem(received)) {
          return true;
        } else {
          counterparty.retrieveItem(countIndex); // rollback
          initiator.addItem(given);
        }
      }
    }
    return false;
  }

  public boolean tradeItemForGold(Inventory initiator, int itemIndex, int itemAmount, Inventory counterparty,
      int goldAmount) {
    ItemStack offer = initiator.getItem(itemIndex); // peek, don't take
    if (offer == null || offer.getQuantity() < itemAmount) {
      return false;
    }

    int offerValue = offer.getItemPrice() * itemAmount;

    if (offerValue >= goldAmount && counterparty.getCurrency() >= goldAmount) {
      ItemStack itemsToTrade = initiator.retrieveItem(itemIndex, itemAmount);

      counterparty.removeCurrency(goldAmount);
      initiator.addCurrency(goldAmount);

      if (counterparty.addItem(itemsToTrade)) {
        return true;
      } else {
        // rollback
        initiator.removeCurrency(goldAmount);
        counterparty.addCurrency(goldAmount);
        initiator.addItem(itemsToTrade);
      }
    }
    return false;
  }

  public boolean tradeGoldForItem(Inventory initiator, int goldAmount, Inventory counterparty, int itemIndex,
      int itemAmount) {
    ItemStack request = counterparty.getItem(itemIndex);
    if (request == null || request.getQuantity() < itemAmount)
      return false;

    int totalValue = request.getItemPrice() * itemAmount;

    if (goldAmount >= totalValue && initiator.getCurrency() >= goldAmount) {
      ItemStack received = counterparty.retrieveItem(itemIndex, itemAmount);

      initiator.removeCurrency(goldAmount);
      counterparty.addCurrency(goldAmount);

      if (initiator.addItem(received)) {
        return true;
      } else {
        // rollback
        initiator.addCurrency(goldAmount);
        counterparty.removeCurrency(goldAmount);
        counterparty.addItem(received);
      }
    }
    return false;
  }

  public boolean tradeGoldForGold(Inventory initiator, int offerAmount, Inventory counterparty, int requestAmount) {
    if (initiator.getCurrency() >= offerAmount && counterparty.getCurrency() >= requestAmount) {
      if (offerAmount >= requestAmount) {
        initiator.removeCurrency(offerAmount);
        counterparty.addCurrency(offerAmount);

        counterparty.removeCurrency(requestAmount);
        initiator.addCurrency(requestAmount);
        return true;
      }
    }
    return false;
  }
}
