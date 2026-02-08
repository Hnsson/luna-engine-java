package com.engine.level;

import java.util.List;
import com.engine.ecs.Entity;
import com.engine.rendering.RenderLayerRegistry.Layers;

public class LevelData {
  public String levelName;

  public TilemapData tilemap;

  public List<Entity> entities;

  public static class TilemapData {
    public List<LayerData> layers;
  }

  public static class LayerData {
    public String name;
    public int tileSize;
    public String spriteId;
    public Layers layer;
    public List<List<Integer>> data;
  }
}
