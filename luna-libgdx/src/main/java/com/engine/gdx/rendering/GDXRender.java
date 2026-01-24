package com.engine.gdx.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.utils.Disposable;
import com.engine.rendering.contexts.IRenderContext;
import com.engine.rendering.models.SpriteDefinition;

public class GDXRender implements IRenderContext, Disposable {
  private GDXAssetManager assetManager;

  private OrthographicCamera camera;
  private OrthographicCamera uiCamera;

  private ShapeRenderer shapeRenderer;
  private SpriteBatch batch = new SpriteBatch();

  private BitmapFont font;
  private GlyphLayout layout;

  public GDXRender(GDXAssetManager assetManager, OrthographicCamera camera) {
    this.assetManager = assetManager;
    this.camera = camera;
    this.uiCamera = new OrthographicCamera();
    this.uiCamera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    this.shapeRenderer = new ShapeRenderer();
    this.font = new BitmapFont();
    this.layout = new GlyphLayout();
  }

  @Override
  public void clearScreen(float r, float g, float b) {
    Gdx.gl.glClearColor(r, g, b, 1);
    Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
  }

  @Override
  public void beginFrame() {
    batch.setProjectionMatrix(camera.combined);
    batch.begin();
  }

  @Override
  public void endFrame() {
    batch.end();
  }

  @Override
  public void beginDebugFrame() {
    if (batch.isDrawing())
      batch.end();
    shapeRenderer.setProjectionMatrix(camera.combined);
    shapeRenderer.setAutoShapeType(true);
    shapeRenderer.begin(ShapeType.Line);
  }

  @Override
  public void endDebugFrame() {
    shapeRenderer.end();
  }

  @Override
  public void drawSprite(SpriteDefinition def, float worldX, float worldY, float width, float height, boolean flipX,
      boolean flipY) {
    Texture texture = assetManager.get(def.texturePath);
    if (texture == null) {
      System.out.println("[ERROR::GDXRENDER] Texture is null for " + def.texturePath);
      return;
    }

    batch.draw(
        texture,
        worldX, worldY,
        width, height, def.x, def.y,
        def.width, def.height,
        flipX, flipY);
  }

  @Override
  public void drawRectangle(float x, float y, float w, float h, float r, float g, float b, float a) {
    shapeRenderer.set(ShapeType.Line);
    shapeRenderer.setColor(r, g, b, a);
    shapeRenderer.rect(x, y, w, h);
  }

  @Override
  public void drawFilledRectangle(float x, float y, float w, float h, float r, float g, float b, float a) {
    shapeRenderer.set(ShapeType.Filled);
    shapeRenderer.setColor(r, g, b, a);
    shapeRenderer.rect(x, y, w, h);
  }

  @Override
  public void beginUIFrame() {
    batch.setProjectionMatrix(uiCamera.combined);
    batch.begin();
  }

  @Override
  public void endUIFrame() {
    batch.end();
  }

  @Override
  public float getTextWidth(String text) {
    layout.setText(font, text);
    return layout.width;
  }

  @Override
  public void drawText(String text, float x, float y, float r, float g, float b, float a) {
    font.setColor(r, g, b, a);
    font.draw(batch, text, x, y);
  }

  @Override
  public void resize(int width, int height) {
    uiCamera.setToOrtho(false, width, height);
    uiCamera.update();
  }

  @Override
  public void dispose() {
    if (batch != null) {
      batch.dispose();
    }
    if (shapeRenderer != null) {
      shapeRenderer.dispose();
    }
    if (font != null) {
      font.dispose();
    }
  }
}
