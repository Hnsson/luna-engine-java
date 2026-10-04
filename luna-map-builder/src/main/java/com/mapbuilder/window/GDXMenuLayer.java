package com.mapbuilder.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.engine.fsm.StateMachine;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.rendering.GDXAssetManager;

public class GDXMenuLayer extends WindowLayer {
  private Stage stage;
  private StateMachine<WindowLayer> fsm;
  private GDXAssetManager assetManager;
  private Skin skin;

  public GDXMenuLayer(int width, int height, StateMachine<WindowLayer> fsm, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.fsm = fsm;
    this.assetManager = assetManager;
    this.stage = new Stage(new ScreenViewport());

    initUI();
  }

  private void initUI() {
    this.skin = createBasicSkin();

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    Label titleLabel = new Label("Luna Map Builder", skin, "title");

    // New Map Button
    TextButton createMapBtn = new TextButton("Create New Map", skin);
    createMapBtn.addListener(new ClickListener() {
      @Override
      public void clicked(InputEvent event, float x, float y) {
        fsm.popLayer();
        fsm.pushLayer(new GDXMapEditorLayer(getWidth(), getHeight(), "NEW_MAP", assetManager));
      }
    });

    // Load Map Button (disabled)
    TextButton loadMapBtn = new TextButton("Load Map", skin);
    loadMapBtn.setDisabled(true);

    // Layout
    rootTable.add(titleLabel).padBottom(40f).row();
    rootTable.add(createMapBtn).width(260f).height(48f).padBottom(15f).row();
    rootTable.add(loadMapBtn).width(260f).height(48f).row();

    stage.addActor(rootTable);
  }

  @Override
  public String getId() {
    return "MAPBUILDER_MENU_LAYER";
  }

  @Override
  public void enter() {
    Gdx.input.setInputProcessor(stage);
  }

  @Override
  public void exit() {
    if (Gdx.input.getInputProcessor() == stage) {
      Gdx.input.setInputProcessor(null);
    }
  }

  @Override
  public void resize(int width, int height) {
    stage.getViewport().update(width, height, true);
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

    Gdx.gl.glClearColor(0.12f, 0.12f, 0.14f, 1f);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
    stage.draw();
  }

  @Override
  public void update(float delta) {
    stage.act(delta);
  }

  @Override
  public void eventHandler() {
    // Picking between the buttons
    // 1. Picking "Create new map" -> Load MapEditorLayer without map data
    // 2. Picking "Load map" (map_name) -> Load MapEditorLayer with map data
  }

  private Skin createBasicSkin() {
    Skin skin = new Skin();

    BitmapFont font = new BitmapFont();
    font.getRegion().getTexture().setFilter(
        Texture.TextureFilter.Nearest,
        Texture.TextureFilter.Nearest);
    skin.add("default-font", font);

    Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
    pixmap.setColor(Color.WHITE);
    pixmap.fill();
    Texture whiteTex = new Texture(pixmap);
    pixmap.dispose();

    TextureRegionDrawable baseDrawable = new TextureRegionDrawable(new TextureRegion(whiteTex));

    // Label
    Label.LabelStyle labelStyle = new Label.LabelStyle(font, Color.WHITE);
    skin.add("default", labelStyle);

    Label.LabelStyle titleStyle = new Label.LabelStyle(font, new Color(0.42f, 0.73f, 0.92f, 1f));
    skin.add("title", titleStyle);

    // Button
    Color primaryColor = new Color(0.42f, 0.73f, 0.92f, 1f);

    TextButton.TextButtonStyle defaultBtnStyle = new TextButton.TextButtonStyle();
    defaultBtnStyle.font = font;
    defaultBtnStyle.up = baseDrawable.tint(primaryColor);
    defaultBtnStyle.down = baseDrawable.tint(new Color(0.32f, 0.60f, 0.78f, 1f));
    defaultBtnStyle.over = baseDrawable.tint(new Color(0.50f, 0.78f, 0.95f, 1f));
    defaultBtnStyle.disabled = baseDrawable.tint(new Color(0.25f, 0.35f, 0.42f, 0.6f));
    defaultBtnStyle.fontColor = Color.BLACK;
    defaultBtnStyle.disabledFontColor = new Color(0.6f, 0.6f, 0.6f, 1f);

    skin.add("default", defaultBtnStyle);

    TextButton.TextButtonStyle secondaryBtnStyle = new TextButton.TextButtonStyle();
    secondaryBtnStyle.font = font;
    secondaryBtnStyle.up = baseDrawable.tint(new Color(0.22f, 0.22f, 0.25f, 1f));
    secondaryBtnStyle.down = baseDrawable.tint(new Color(0.16f, 0.16f, 0.18f, 1f));
    secondaryBtnStyle.over = baseDrawable.tint(new Color(0.28f, 0.28f, 0.32f, 1f));
    secondaryBtnStyle.disabled = baseDrawable.tint(new Color(0.18f, 0.18f, 0.20f, 0.5f));
    secondaryBtnStyle.fontColor = new Color(0.85f, 0.85f, 0.85f, 1f);
    secondaryBtnStyle.disabledFontColor = new Color(0.45f, 0.45f, 0.45f, 1f);

    skin.add("secondary-button", secondaryBtnStyle);

    return skin;
  }

  public void dispose() {
    stage.dispose();
    if (skin != null) {
      skin.dispose();
    }
  }
}
