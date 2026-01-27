package com.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.engine.gdx.rendering.GDXAssetManager;
import com.engine.gdx.states.window.GDXOverlayLayer;
import com.engine.gdx.states.window.GDXLoadingLayer;
import com.engine.fsm.StateMachine;
import com.engine.fsm.states.WindowLayer;

// QUESTION FOR SELF: maybe use Game instead of ApplicationAdapter so I can use Screen and setScreen
public class Sandbox extends ApplicationAdapter {
  private StateMachine<WindowLayer> fsm;
  private GDXAssetManager assetManager;
  private GDXOverlayLayer overlay;

  @Override
  public void create() {
    fsm = new StateMachine();
    assetManager = new GDXAssetManager();
    // load all common assets that will be consitent across all levels (so far
    // player, will probably be more in future)
    assetManager.loadAllTextures("assets/common");
    assetManager.finishLoading();
    assetManager.registerAllTextures();
    //
    fsm.pushLayer(new GDXLoadingLayer(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, "level_one", fsm,
        assetManager));
    overlay = new GDXOverlayLayer(DesktopLauncher.WINDOW_WIDTH, DesktopLauncher.WINDOW_HEIGHT, fsm);
    overlay.enter();
  }

  @Override
  public void resize(int width, int height) {
    fsm.getCurrentState().resize(width, height);
    overlay.resize(width, height);
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
    overlay.eventHandler();
  }

  private void update(float delta) {
    fsm.getCurrentState().update(delta);
    overlay.update(delta);
  }

  private void renderGDX() {
    fsm.getCurrentState().render();
    overlay.render();
  }

  @Override
  public void dispose() {
    System.exit(0); // This fixed the Segmentation Fault due to being in WSL
  }
}
