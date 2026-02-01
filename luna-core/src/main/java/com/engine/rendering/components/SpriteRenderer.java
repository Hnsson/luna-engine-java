package com.engine.rendering.components;

import com.engine.ecs.Component;

/*
 * This is a ECS component.
 *
 * The spriteId is the key to the SpriteRegistry value:
 * SpriteRenderer: "orc_1" ->
 * SpriteDefintion: "file/path" ->
 * AssetManager -->
 * Draw
 */
public class SpriteRenderer extends Component {
  public String spriteId;

  public float width;
  public float height;

  public boolean flipX = false;
  public boolean flipY = false;

  public float xOffset = 0;
  public float yOffset = 0;

  public int currentFrameIndex = 0;

  public SpriteRenderer() {
    this("default", 0, 0, 0, 0, 0);
  }

  public SpriteRenderer(String spriteId, float width, float height) {
    this(spriteId, width, height, 0, 0, 0);
  }

  public SpriteRenderer(String spriteId, float width, float height, float xOffset, float yOffset,
      int currentFrameIndex) {
    this.spriteId = spriteId;
    this.width = width;
    this.height = height;
    this.xOffset = xOffset;
    this.yOffset = yOffset;
    this.currentFrameIndex = currentFrameIndex;
  }
}
