package com.engine.gdx.states.window;

import com.engine.fsm.StateMachine;
import com.engine.fsm.states.WindowLayer;

public class GDXOverlayLayer extends WindowLayer {
  private StateMachine fsm;

  public GDXOverlayLayer(int width, int height, StateMachine fsm) {
    super(width, height); // pass it up
    this.fsm = fsm;
  }

  @Override
  public String getId() {
    return "OVERLAY_LAYER";
  }

  @Override
  public void enter() {

  }

  @Override
  public void exit() {

  }

  @Override
  public void resize(int width, int height) {

  }

  @Override
  public void render() {

  }

  @Override
  public void update(float delta) {

  }

  @Override
  public void eventHandler() {

  }
}
