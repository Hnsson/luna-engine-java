package com.engine.level;

import java.util.List;

import com.engine.level.WorldLoader;
import com.engine.level.map.LevelMap;
import com.engine.level.map.LevelMap.MapTile;
import com.engine.level.map.LevelMap.MapLayer;

// Have to think about this implementation, so if I load the world based on the provided tilemap and sizes,
// like should it be like world size or like fit to screen size, because if it should load in based on a "global" scale,
// then I need to implement cameras which would be nice if I have a camera that can be attached to an entity and follows it,
// because then I can move a camera object from entitiy to entity, like if player enters arena,
// disable his movements and move camera object to boss and then back to player and allow him to move.
public class WorldLoader {
  public LevelMap loadMap(LevelData data) {
    LevelMap map = new LevelMap();
    if (data.tilemap == null || data.tilemap.layers == null)
      return map;

    for (LevelData.LayerData rawLayer : data.tilemap.layers) {
      MapLayer layer = new MapLayer();
      layer.spriteId = rawLayer.spriteId;
      layer.tileSize = rawLayer.tileSize;
      layer.renderLayer = rawLayer.layer;

      int rowCount = rawLayer.data.size();

      for (int i = 0; i < rowCount; i++) {
        List<Integer> row = rawLayer.data.get(i);

        for (int j = 0; j < row.size(); j++) {
          int tileId = row.get(j);
          if (tileId == 0)
            continue;

          float worldX = j * rawLayer.tileSize;
          float worldY = (rowCount - 1 - i) * rawLayer.tileSize;
          int renderIndex = tileId - 1;

          layer.tiles.add(new MapTile(worldX, worldY, renderIndex));
        }
      }
      map.layers.add(layer);
    }
    return map;
  }
}
