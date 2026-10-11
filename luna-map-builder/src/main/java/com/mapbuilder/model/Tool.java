package com.mapbuilder.model;

enum LayerIndex {
  GROUND,
  COLLIDER,
  DECORATIONS
};

public class Tool {
  enum Tools {
    SELECT,
    PAINT,
    ERASE,
    FILL
  };

  private Tools currentTool;

  public Tool() {
    this.currentTool = Tools.PAINT;
  }

  public Tools getActiveTool() {
    return this.currentTool;
  }

  public void setActiveTool(Tools tool) {
    this.currentTool = tool;
  }
}
