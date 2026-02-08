package com.engine.level;

import com.engine.level.map.LevelMap;

public interface WorldLoader {
  LevelMap loadMap(LevelData data);
}
