package com.engine.rendering;

import java.util.HashMap;
import java.util.Map;

public class RenderLayerRegistry {
  private static final Map<Layers, Integer> layers = new HashMap<>();
  private static final int DEFAULT_LAYER = 0;

  public static enum Layers {
    BACKGROUND, DEFAULT, PLAYER, FOREGROUND, UI
  };

  static {
    layers.put(Layers.BACKGROUND, -100);
    layers.put(Layers.DEFAULT, 0);
    layers.put(Layers.PLAYER, 1);
    layers.put(Layers.FOREGROUND, 10);
    layers.put(Layers.UI, 100);
  }

  public static int getOrder(Layers layerName) {
    return layers.getOrDefault(layerName, DEFAULT_LAYER);
  }
}
