package com.mapbuilder.model;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EditorModel {
  private int[][] map;
  private int[][] tileMap;
  private int tileSize;
  private Texture loadedTexture;
  private TextureRegion[][] loadedTextureRegion;

  private int selectedTileId;
  private TextureRegion selectedRegion;
  private Tool currentTool;

  public EditorModel() {

  }

  public void setSelectedTile(int id) {

  }

  public void setTileAt(int x, int y, int id) {

  }
}
