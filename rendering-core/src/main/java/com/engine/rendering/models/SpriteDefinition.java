package com.engine.rendering.models;

/*
 * Will be like my ItemDefintion it will be the singular definition of a sprite
 * location in spritesheet. So a sprite will have a texturePath which will
 * with the help of the assetmanager lead to a file, and a specific x,y.
 * So if developer choose to have a single .png for a sprite its just,
 * x and y = 0 while it has a width and height and a path to file or if
 * they want a spritesheet, the x and y will help guide where in it.
 */
public class SpriteDefinition {
  public String texturePath;

  public int x, y;
  public int width, height;

  public SpriteDefinition(String path, int x, int y, int width, int height) {
    this.texturePath = path;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }
}
