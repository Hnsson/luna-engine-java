package com.mapbuilder.ui;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;
import com.mapbuilder.model.EditorModel;

public class CanvasPanel extends Widget {
  private final EditorModel model;
  private final OrthographicCamera canvasCam;
  private final ShapeRenderer shapeRenderer;

  public CanvasPanel(EditorModel model) {
    this.model = model;
    this.canvasCam = new OrthographicCamera();
    this.shapeRenderer = new ShapeRenderer();

    initListeners();
  }

  private void initListeners() {

  }
}
