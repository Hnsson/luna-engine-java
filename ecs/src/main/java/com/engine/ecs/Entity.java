package com.engine.ecs;

import com.engine.math.Vector2;

public class Entity {
    public String name;
    public Vector2 position;

    public Entity(String name) {
        this.name = name;
        this.position = new Vector2(0, 0);
    }
}
