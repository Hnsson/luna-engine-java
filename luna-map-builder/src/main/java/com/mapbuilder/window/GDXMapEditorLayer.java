package com.mapbuilder.window;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.rendering.GDXAssetManager;

public class GDXMapEditorLayer extends WindowLayer {
  private String mapName;

  private OrthographicCamera camera;
  private Viewport viewport;

  private GDXAssetManager assetManager;

  public GDXMapEditorLayer(int width, int height, String mapName, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.mapName = mapName;
    this.assetManager = assetManager;
  }

  @Override
  public String getId() {
    return "MAPBUILDER_MAP_EDITOR_LAYER";
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
