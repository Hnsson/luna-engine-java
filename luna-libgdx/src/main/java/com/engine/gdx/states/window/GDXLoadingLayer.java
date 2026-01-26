package com.engine.gdx.states.window;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.fsm.StateMachine;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.rendering.GDXAssetManager;
import com.engine.gdx.rendering.GDXRender;

public class GDXLoadingLayer extends WindowLayer {
  private GDXAssetManager assetManager;
  private StateMachine<WindowLayer> fsm;

  private OrthographicCamera camera;
  private Viewport viewport;
  private GDXRender renderer;

  public GDXLoadingLayer(int width, int height, StateMachine<WindowLayer> fsm, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.fsm = fsm;
    this.assetManager = assetManager;
  }

  @Override
  public String getId() {
    return "LOADING_LAYER";
  }

  @Override
  public void enter() {
    camera = new OrthographicCamera();
    viewport = new FitViewport(this.width, this.height, camera);
    renderer = new GDXRender(assetManager, camera);

    System.out.println("Loading assets...");
    assetManager.loadAllTextures("assets/sprites");
  }

  @Override
  public void exit() {

  }

  @Override
  public void resize(int width, int height) {
    viewport.update(width, height, true);
    renderer.resize(width, height);
  }

  @Override
  public void render() {
    renderer.clearScreen(0, 0, 0);
    float progress = assetManager.getProgress();

    int barWidth = 400;
    int barHeight = 40;
    float x = (width - barWidth) / 2f;
    float y = (height - barHeight) / 2f;

    renderer.beginUIShapeFrame();
    renderer.drawProgressbar(x, y, barWidth, barHeight, progress, 0.5f, 0.5f, 0.5f, 1f, 0f, 1f, 0f, 1f);
    renderer.endUIShapeFrame();
  }

  @Override
  public void update(float delta) {
    boolean isDone = assetManager.updateLoading();
    if (isDone) {
      assetManager.registerAllTextures();
      System.out.println("Assets loaded");
      fsm.popLayer();
      fsm.pushLayer(new GDXGameLayer(width, height, assetManager));
    }
  }

  @Override
  public void eventHandler() {

  }
}
