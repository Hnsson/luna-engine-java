package com.mapbuilder.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.mapbuilder.model.EditorModel;
import com.mapbuilder.model.EditorModelListener;
import com.mapbuilder.model.FileDialog;
import com.mapbuilder.model.Tool;

public class PalettePanel extends Table implements EditorModelListener {
  private final EditorModel model;
  private final Skin skin;
  private Table tileGridTable;
  private ButtonGroup<ImageButton> tileButtonGroup;

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
        new Thread(() -> {
          loadTileset();
        }).start();
      }
    });
    TextButton refreshTileset = new TextButton("Refresh", skin, "secondary-button");
    refreshTileset.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        refreshTileset();
      }
    });

    this.tileGridTable = initTilesetUI();
    ScrollPane scrollPane = new ScrollPane(tileGridTable, skin);
    scrollPane.setScrollingDisabled(false, false);
    scrollPane.setFadeScrollBars(false);

    add(titleLabel).left().pad(10f, 4f, 10f, 10f).colspan(2).row();

    add(addTileMap).height(48f).padRight(5f).padBottom(5f).growX();
    add(refreshTileset).height(48f).padBottom(5f).growX().row();

    add(scrollPane).colspan(2).grow().pad(4f);
  }

  private Table initTilesetUI() {
    Table grid = new Table();
    grid.top().left();
    grid.defaults().pad(0f).space(0f);

    Label placeholder = new Label("No tileset loaded.\nClick '+ Tilemap' above.", skin);
    placeholder.setName("placeholder");
    placeholder.setAlignment(Align.center);
    grid.add(placeholder).expand().center().pad(10f);

    return grid;
  }

  @Override
  public void onTilesetLoaded(TextureRegion[][] regions, int tileWidth, int tileHeight) {
    tileGridTable.clearChildren();

    if (regions == null || regions.length == 0) {
      return;
    }

    // max 1 select and min 0
    tileButtonGroup = new ButtonGroup<>();
    tileButtonGroup.setMinCheckCount(0);
    tileButtonGroup.setMaxCheckCount(1);

    int totalCols = regions[0].length;
    float displaySize = Math.max(32f, Math.max(tileWidth, tileHeight) * 2f);

    Drawable selectionBorder = skin.getDrawable("tile-selected-border");

    for (int row = 0; row < regions.length; row++) {
      for (int col = 0; col < regions[row].length; col++) {
        TextureRegion region = regions[row][col];
        final int tileId = (row * totalCols) + col + 1;
        final int r = row;
        final int c = col;

        ImageButton.ImageButtonStyle btnStyle = new ImageButton.ImageButtonStyle();
        btnStyle.imageUp = new TextureRegionDrawable(region);
        btnStyle.checked = selectionBorder;

        ImageButton tileBtn = new ImageButton(btnStyle);
        tileBtn.pad(1.5f);
        tileBtn.setUserObject(tileId);
        tileBtn.getImage().setScaling(Scaling.stretch);
        tileBtn.getImageCell().grow();

        tileButtonGroup.add(tileBtn);

        tileBtn.addListener(new ClickListener() {
          @Override
          public void clicked(InputEvent event, float x, float y) {
            model.setSelectedTile(r, c);
          }
        });

        tileGridTable.add(tileBtn).size(displaySize).pad(0f);
      }
      tileGridTable.row();
    }
  }

  @Override
  public void onTileSelected(int tileId, TextureRegion region) {
    if (tileButtonGroup == null) {
      return;
    }

    if (tileId <= 0) {
      tileButtonGroup.uncheckAll();
      return;
    }

    for (ImageButton btn : tileButtonGroup.getButtons()) {
      Object tag = btn.getUserObject();
      if (tag instanceof Integer && (int) tag == tileId) {
        btn.setChecked(true);
        break;
      }
    }
  }

  @Override
  public void onTilePlaced(int x, int y, int tileId) {

  }

  @Override
  public void onToolChanged(Tool newTool) {

  }

  private void loadTileset() {
    String[] filters = new String[] { "*.png", "*.jpg", "*.jpeg" };

    String selectedFile = FileDialog.openFileDialog("Select Tileset Image", null, filters,
        "Image Files (*.png, *.jpg, *.jpeg)", false);

    if (selectedFile != null) {
      Gdx.app.postRunnable(() -> {
        loadTilesetFile(Gdx.files.absolute(selectedFile));
      });
    }
  }

  private void loadTilesetFile(FileHandle handle) {
    if (!handle.exists()) {
      return;
    }

    Texture tilesetTexture = new Texture(handle);
    // TODO: Ask user to enter width and height before this
    int tileWidth = 32;
    int tileHeight = 32;

    TextureRegion[][] splitTiles = TextureRegion.split(tilesetTexture, tileWidth, tileHeight);
    model.setTileset(tilesetTexture, splitTiles, tileWidth, tileHeight);
  }

  private void refreshTileset() {

  }
}
