package com.mapbuilder;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.engine.fsm.StateMachine;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.rendering.GDXAssetManager;
import com.mapbuilder.window.GDXMenuLayer;

public class MapBuilder extends ApplicationAdapter {
  private StateMachine<WindowLayer> fsm;
  private GDXAssetManager assetManager;

  @Override
  public void create() {
    fsm = new StateMachine<>();

    assetManager = new GDXAssetManager();
    assetManager.loadAllTextures("assets/common");
    assetManager.finishLoading();
    assetManager.registerAllTextures();

    fsm.pushLayer(new GDXMenuLayer(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, fsm, assetManager));
  }

  @Override
  public void resize(int width, int height) {
    fsm.getCurrentState().resize(width, height);
  }

  @Override
  public void render() {
    // The looping function from libGDX
    float delta = Gdx.graphics.getDeltaTime();

    eventHandler();
    update(delta);
    renderGDX();
  }

  private void eventHandler() {
    fsm.getCurrentState().eventHandler();
  }

  private void update(float delta) {
    fsm.getCurrentState().update(delta);
  }

  private void renderGDX() {
    fsm.getCurrentState().render();
  }

  @Override
  public void dispose() {
    System.exit(0); // This fixed the Segmentation Fault due to being in WSL
  }
}
