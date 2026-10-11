package com.mapbuilder.model;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class EditorModel {
  private ArrayList<EditorModelListener> listeners;
  private int[][] map;
  private int[][] tileMap;
  private int tileWidth;
  private int tileHeight;
  private Texture loadedTexture;
  private TextureRegion[][] loadedTextureRegion;

  private int selectedTileId;
  private TextureRegion selectedRegion;
  private Tool currentTool;

  public EditorModel() {
    this.listeners = new ArrayList<EditorModelListener>();
    this.currentTool = new Tool();
  }

  public void registerListener(EditorModelListener listener) {
    this.listeners.add(listener);
  }

  public void removeListener(EditorModelListener listener) {
    this.listeners.remove(listener);
  }

  public TextureRegion getRegionForId(int tileId) {
    if (tileId <= 0 || loadedTextureRegion == null || loadedTextureRegion.length == 0) {
      return null;
    }

    int totalCols = loadedTextureRegion[0].length;
    int index = tileId - 1;
    int row = index / totalCols;
    int col = index % totalCols;

    if (row < 0 || row >= loadedTextureRegion.length || col < 0 || col >= totalCols) {
      return null;
    }

    return loadedTextureRegion[row][col];
  }

  public void setSelectedTile(int row, int col) {
    int totalCols = loadedTexture.getWidth() / tileWidth;
    this.selectedTileId = (row * totalCols) + col + 1;
    this.selectedRegion = loadedTextureRegion[row][col];

    for (EditorModelListener listener : listeners) {
      listener.onTileSelected(this.selectedTileId, this.selectedRegion);
    }
  }

  public void setTileAt(int x, int y, int id) {

  }

  public void setTileset(Texture texture, TextureRegion[][] textureRegion, int tileWidth, int tileHeight) {
    if (this.loadedTexture != null) {
      this.loadedTexture.dispose();
    }
    this.loadedTexture = texture;
    this.loadedTextureRegion = textureRegion;
    this.tileHeight = tileHeight;
    this.tileWidth = tileWidth;

    for (EditorModelListener listener : listeners) {
      listener.onTilesetLoaded(this.loadedTextureRegion, this.tileWidth, this.tileHeight);
    }
  }

  public void beautyPrint() {
    System.out.println("Current tool:\t\t\t" + currentTool.getActiveTool());
    System.out.println("(tileWidth, tileHeight):\t" + "(" + tileWidth + ", " + tileHeight + ")");
    System.out.println("selectedTileId:\t\t\t" + selectedTileId);

    System.out.println();
  }

}
