package com.engine.fsm.states;

/*
 * Going with abstract instead of another extending
 * interface because I need to enforce state, i.e.
 * I want to have layers with width and height
 */
public abstract class WindowLayer implements Layer {
  protected int width;
  protected int height;

  public WindowLayer(int width, int height) {
    this.width = width;
    this.height = height;
  }

  // So all WindowLayer children gets these:
  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public abstract String getId();

  public abstract void render();

  public abstract void update(float delta);

  public abstract void eventHandler();

  public abstract void resize(int width, int height);
}
