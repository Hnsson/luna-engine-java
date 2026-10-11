package com.mapbuilder.window;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.SplitPane.SplitPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.engine.fsm.states.WindowLayer;
import com.engine.gdx.rendering.GDXAssetManager;
import com.mapbuilder.model.EditorModel;
import com.mapbuilder.ui.CanvasPanel;
import com.mapbuilder.ui.PalettePanel;

public class GDXMapEditorLayer extends WindowLayer {
  private String mapName;
  private GDXAssetManager assetManager;

  private Stage stage;
  private EditorModel model;
  private PalettePanel palettePanel;
  private CanvasPanel canvasPanel;

  private Skin skin;

  public GDXMapEditorLayer(int width, int height, String mapName, GDXAssetManager assetManager) {
    super(width, height); // pass it up
    this.mapName = mapName;
    this.assetManager = assetManager;

    stage = new Stage(new ScreenViewport());
    model = new EditorModel();

    this.skin = createSkin();
    this.palettePanel = new PalettePanel(model, skin);
    this.canvasPanel = new CanvasPanel(model);

    model.registerListener(this.palettePanel);

    Table rootTable = new Table();
    rootTable.setFillParent(true);

    SplitPane split = new SplitPane(palettePanel, canvasPanel, false, skin);
    split.setSplitAmount(0.30f); // 20% left, 80% right
    split.setMinSplitAmount(0.25f);
    split.setMaxSplitAmount(0.50f);

    rootTable.add(split).grow();
    stage.addActor(rootTable);

    Gdx.input.setInputProcessor(stage);
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
    stage.getViewport().update(width, height, true);
  }

  @Override
  public void render() {
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

  }

  private Skin createSkin() {
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

    SplitPaneStyle splitStyle = new SplitPaneStyle();
    splitStyle.handle = baseDrawable.tint(new Color(0.28f, 0.30f, 0.36f, 1f));
    splitStyle.handle.setMinWidth(6f);
    splitStyle.handle.setMinHeight(6f);

    skin.add("default-horizontal", splitStyle);
    skin.add("default-vertical", splitStyle);
    skin.add("panel-palette", baseDrawable.tint(new Color(0.14f, 0.15f, 0.18f, 1f)));

    // ScrollPane
    ScrollPane.ScrollPaneStyle scrollStyle = new ScrollPane.ScrollPaneStyle();
    scrollStyle.background = baseDrawable.tint(new Color(0.11f, 0.12f, 0.14f, 1f));

    Drawable vScrollTrack = baseDrawable.tint(new Color(0.18f, 0.19f, 0.22f, 1f));
    vScrollTrack.setMinWidth(6f);
    scrollStyle.vScroll = vScrollTrack;

    Drawable vScrollKnob = baseDrawable.tint(new Color(0.35f, 0.37f, 0.44f, 1f));
    vScrollKnob.setMinWidth(6f);
    scrollStyle.vScrollKnob = vScrollKnob;

    Drawable hScrollTrack = baseDrawable.tint(new Color(0.18f, 0.19f, 0.22f, 1f));
    hScrollTrack.setMinHeight(6f);
    scrollStyle.hScroll = hScrollTrack;

    Drawable hScrollKnob = baseDrawable.tint(new Color(0.35f, 0.37f, 0.44f, 1f));
    hScrollKnob.setMinHeight(6f);
    scrollStyle.hScrollKnob = hScrollKnob;

    Drawable selectionBorder = baseDrawable.tint(new Color(0.42f, 0.73f, 0.92f, 1f));

    skin.add("default", scrollStyle);
    skin.add("tile-selected-border", selectionBorder, Drawable.class);

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
}
