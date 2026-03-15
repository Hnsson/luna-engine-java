package com.mapbuilder.window;

import com.engine.fsm.StateMachine;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.rendering.GDXAssetManager;

public class GDXMenuLayer extends WindowLayer {
  private StateMachine<WindowLayer> fsm;
  private GDXAssetManager assetManager;

  public GDXMenuLayer(int width, int height, StateMachine<WindowLayer> fsm, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.fsm = fsm;
    this.assetManager = assetManager;
  }

  @Override
  public String getId() {
    return "MAPBUILDER_MENU_LAYER";
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
    // Render two buttons:
    // 1. Create new map
    // 2. Load map
    /*
     * Load map should fetch all the files in the assets/maps/*.json
     * and display their names. These maps should translate to what
     * the luna-engine expects for format. You should be able to edit
     * and create maps by drawing tiles from a tilemap on a grid.
     */
  }

  @Override
  public void update(float delta) {

  }

  @Override
  public void eventHandler() {
    // Picking between the buttons
    // 1. Picking "Create new map" -> Load MapEditorLayer without map data
    // 2. Picking "Load map" (map_name) -> Load MapEditorLayer with map data
  }
}
