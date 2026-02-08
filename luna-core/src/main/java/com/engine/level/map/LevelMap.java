package com.engine.level.map;

import java.util.ArrayList;
import java.util.List;

import com.engine.rendering.RenderLayerRegistry.Layers;

public class LevelMap {
  public List<MapLayer> layers = new ArrayList<>();

  public static class MapLayer {
    public String spriteId;
    public int tileSize;
    public Layers renderLayer;
    public List<MapTile> tiles = new ArrayList<>();
  }

  public static class MapTile {
    public float x, y;
    public int textureIndex;

    public MapTile(float x, float y, int index) {
      this.x = x;
      this.y = y;
      this.textureIndex = index;
    }
  }
}
