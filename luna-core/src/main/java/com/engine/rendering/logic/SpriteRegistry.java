package com.engine.rendering.logic;

import java.util.HashMap;
import java.util.Map;

import com.engine.rendering.models.SpriteDefinition;

public class SpriteRegistry {
  private static final Map<String, SpriteDefinition> sprites = new HashMap<>();

  public static void register(String id, SpriteDefinition def) {
    sprites.put(id, def);
  }

  public static SpriteDefinition get(String id) {
    return sprites.get(id);
  }
}
