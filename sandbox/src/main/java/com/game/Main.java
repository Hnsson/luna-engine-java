package com.game;

import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;
import com.engine.inventory.components.Inventory;

import com.engine.dialogue.DialogueManager;

import com.engine.inventory.logic.ItemRegistry;
import com.engine.inventory.models.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  private static Entity ecs_system() {
    // ----------------------- ECS -----------------------
    Entity player = new Entity();
    System.out.println("Created player");

    Transform t1 = new Transform();
    player.addComponent(t1);

    if (player.hasComponent(Transform.class)) {
      System.out.println("Player successfully added a Transform component!");

      player.getComponent(Transform.class).velocity.x = 1;
      System.out.println(player.getComponent(Transform.class).toString());
      for (int i = 0; i < 20; i++) {
        player.update();
        System.out.println(player.getComponent(Transform.class).toString());
      }
    }

    Entity enemy = new Entity();
    System.out.println("Created enemy");

    Transform t2 = new Transform();
    enemy.addComponent(t2);

    if (enemy.hasComponent(Transform.class)) {
      System.out.println("Enemy successfully added a Transform component!");
      for (int i = 0; i < 20; i++) {
        enemy.update();
        System.out.println(enemy.getComponent(Transform.class).toString());
      }
    }

    return player;
  }

  private static void dialogue_system(Entity player) {
    // ----------------------- Dialogue -----------------------
    DialogueManager manager = new DialogueManager();
    manager.loadAllGraphs("/graphs");
    Scanner scanner = new Scanner(System.in);

    // manager.registerGraph("/graphs/blacksmith_dialogue.json");
    // manager.registerGraph("/graphs/cityguard_dialogue.json");

    Entity blacksmith_NPC = new Entity("[BLACKSMITH] Brokk Ironjaw", "blacksmith_start");
    Entity cityguard_NPC = new Entity("[CITY GUARD] Garrett", "gate_guard_start");

    List<Entity> act1_available_npc = new ArrayList<>();
    act1_available_npc.add(blacksmith_NPC);
    act1_available_npc.add(cityguard_NPC);

    System.out.println("These are the available NPCs:");
    for (int i = 0; i < act1_available_npc.size(); i++) {
      System.out.println("[" + (i + 1) + "] " + act1_available_npc.get(i).getName());
    }

    System.out.println("Which do you wanna talk to? ");
    while (true) {
      if (scanner.hasNextInt()) {
        int choice = scanner.nextInt();
        if (choice > 0 && choice <= act1_available_npc.size()) {
          manager.startDialogue(act1_available_npc.get(choice - 1).getDialogueNodeId(), player);

          while (manager.isActive()) {
            System.out.print("Your choice: ");
            if (scanner.hasNextInt()) {
              int optionChoice = scanner.nextInt();
              manager.chooseOption(optionChoice);
            } else {
              scanner.next();
              System.out.println("Please enter a number.");
            }
          }
          break;

        } else {
          System.out.println("Invalid option. Please pick 1-" + act1_available_npc.size());
          System.out.print("Try again: ");
        }

      } else {
        String trash = scanner.next();
        System.out.println("'" + trash + "' is not a number.");
        System.out.print("Try again: ");
      }
    }
    scanner.close();
  }

  private static void print_inventory(Entity player) {
    List<ItemStack> inventory = player.getComponent(Inventory.class).getInventory();
    System.out.println("--- INVENTORY ---");
    for (int i = 0; i < inventory.size(); i++) {
      ItemStack stack = inventory.get(i);

      System.out.print("[" + i + "]\t");
      if (stack == null) {
        System.out.println("Empty");
      } else {
        System.out.println(stack.getItemName() + " (x" + stack.getQuantity() + ")");
      }
    }
    System.out.println("-----------------");
  }

  private static void inventory_system() {
    // ----------------------- Inventory System -----------------------
    ItemRegistry.loadAllItems("/items/items.json");
    System.out.println("Does this work?");
    ItemStack sword_stack = ItemRegistry.createStack("iron_sword", 13);

    System.out.println("STACK: ");
    System.out.println("\tID: " + sword_stack.getItemId());
    System.out.println("\tNAME: " + sword_stack.getItemName());
    System.out.println("\tITEM DESCRIPTION: " + sword_stack.getItemDescription());
    System.out.println("\tQUANTITY: " + sword_stack.getQuantity());
    System.out.println("\tDURABILITY: " + sword_stack.getCurrentDurability());
    System.out.println("\tMAX DURABILITY: " + sword_stack.getMaxDurability());
    System.out.println("\tITEM PRICE: " + sword_stack.getItemPrice());
    System.out.println("\tSTACK PRICE: " + sword_stack.getStackPrice());

    Entity player = new Entity();
    Inventory inventory = new Inventory(5);

    player.addComponent(inventory);

    print_inventory(player);

    if (player.getComponent(Inventory.class).addItem(sword_stack)) {
      System.out.println("Yippi!");
    } else {
      System.out.println("Awww :(");
    }

    print_inventory(player);

    ItemStack sword_stack_2 = ItemRegistry.createStack("iron_sword", 4);
    player.getComponent(Inventory.class).addItem(sword_stack_2, 1);

    print_inventory(player);

    ItemStack sword_stack_3 = ItemRegistry.createStack("iron_sword", 4);
    player.getComponent(Inventory.class).addItem(sword_stack_2);

    print_inventory(player);
  }

  public static void main(String[] args) {
    // Entity player = ecs_system();
    // dialogue_system(player);

    inventory_system();
  }
}
