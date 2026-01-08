package com.game;

import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;

import com.engine.dialogue.DialogueManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
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

    // Dialogue
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

    // manager.startDialogue(blacksmith_NPC.startDialogueNodeId());
    //
    // while (manager.isActive()) {
    // System.out.print("Your choice: ");
    // if (scanner.hasNextInt()) {
    // int choice = scanner.nextInt();
    // manager.chooseOption(choice);
    // } else {
    // scanner.next();
    // System.out.println("Please enter a number.");
    // }
    // }
    scanner.close();
  }
}
