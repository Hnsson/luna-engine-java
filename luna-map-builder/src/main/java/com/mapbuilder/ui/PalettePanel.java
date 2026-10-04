package com.mapbuilder.ui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.mapbuilder.model.EditorModel;

public class PalettePanel extends Table {
  private final EditorModel model;
  private final Skin skin;

  public PalettePanel(EditorModel model, Skin skin) {
    this.model = model;
    this.skin = skin;

    if (skin.has("panel-palette", Drawable.class)) {
      setBackground(skin.getDrawable("panel-palette"));
    }

    // align top-left instead of center
    top().left();

    initUI();
  }

  private void initUI() {
    Label titleLabel = new Label("Tileset Palette", skin, "title");

    TextButton addTileMap = new TextButton("+ Tilemap", skin);
    addTileMap.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        loadTilemap();
      }
    });
    TextButton refreshTilemap = new TextButton("Refresh", skin, "secondary-button");
    refreshTilemap.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        refreshTilemap();
      }
    });

    add(titleLabel).left().pad(10f, 4f, 10f, 10f).colspan(2).row();

    add(addTileMap).height(48f).padRight(5f).padBottom(5f).growX();
    add(refreshTilemap).height(48f).padBottom(5f).growX().row();
  }

  private void loadTilemap() {

  }

  private void refreshTilemap() {

  }
}
