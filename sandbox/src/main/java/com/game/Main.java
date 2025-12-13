package com.game;

import com.engine.ecs.Entity;
import com.engine.math.Vector2;

public class Main {
    public static void main(String[] args) {
        Entity player = new Entity("Player1");
        player.position = new Vector2(10, 5);

        System.out.println("Created " + player.name + " at " + player.position);
    }
}
