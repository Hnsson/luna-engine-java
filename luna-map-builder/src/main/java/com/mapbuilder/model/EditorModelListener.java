package com.mapbuilder.model;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public interface EditorModelListener {
  void onTilesetLoaded(TextureRegion[][] regions, int tileWidth, int tileHeight);

  void onTileSelected(int tileId, TextureRegion region);

  void onTilePlaced(int x, int y, int tileId);

  void onToolChanged(Tool newTool);
}
