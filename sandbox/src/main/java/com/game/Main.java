package com.game;

import com.engine.ecs.Entity;
import com.engine.ecs.components.Transform;

import com.engine.dialogue.DialogueManager;
import java.util.Scanner;

public class Main {
  public static void main(String[] args) {
    Entity player = new Entity();
    System.out.println("Created player");

    Transform t1 = new Transform();
    player.addComponent(t1);

    if (player.hasComponent(Transform.class)) {
      System.out.println("Player successfully added a Transform component!");
      for (int i = 0; i < 20; i++) {
        player.update();
        player.getComponent(Transform.class).velocity.x = 1;
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
        // enemy.getComponent(Transform.class).velocity.x = 1;
        System.out.println(enemy.getComponent(Transform.class).toString());
      }
    }

    if (player.getComponent(Transform.class) == enemy.getComponent(Transform.class)) {
      System.out.println("Yo");
    } else {
      System.out.println("No");
    }

    // Dialogue
    DialogueManager manager = new DialogueManager();
    Scanner scanner = new Scanner(System.in);

    manager.registerGraph("/graphs/blacksmith_dialogue.json");
    Entity blacksmith_NPC = new Entity("blacksmith_start");

    manager.startDialogue(blacksmith_NPC.startDialogueNodeId());

    while (manager.isActive()) {
      System.out.print("Your choice: ");
      if (scanner.hasNextInt()) {
        int choice = scanner.nextInt();
        manager.chooseOption(choice);
      } else {
        scanner.next();
        System.out.println("Please enter a number.");
      }
    }
  }
}
