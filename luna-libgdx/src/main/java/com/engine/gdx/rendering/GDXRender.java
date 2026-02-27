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
    if (shapeRenderer.isDrawing())
      shapeRenderer.end();
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
  public void beginUIFrame() {
    if (shapeRenderer.isDrawing())
      shapeRenderer.end();
    batch.setProjectionMatrix(uiCamera.combined);
    batch.begin();
  }

  @Override
  public void endUIFrame() {
    batch.end();
  }

  @Override
  public void beginUIShapeFrame() {
    if (batch.isDrawing())
      batch.end();

    Gdx.gl.glEnable(GL20.GL_BLEND);
    Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

    shapeRenderer.setProjectionMatrix(uiCamera.combined);
    shapeRenderer.setAutoShapeType(true);
    shapeRenderer.begin(ShapeType.Filled);
  }

  @Override
  public void endUIShapeFrame() {
    shapeRenderer.end();
    Gdx.gl.glDisable(GL20.GL_BLEND);
  }

  @Override
  public void drawSprite(SpriteDefinition def, int frameIndex, float worldX, float worldY, float width, float height,
      boolean flipX, boolean flipY) {
    Texture texture = assetManager.get(def.texturePath);
    if (texture == null) {
      System.out.println("[ERROR::GDXRENDER] Texture is null for " + def.texturePath);
      return;
    }

    int srcX, srcY, srcW, srcH;

    // Like in spritedef (grid spritesheet)
    if (def.isSpritesheet) {
      srcW = def.width;
      srcH = def.height;

      int cols = texture.getWidth() / srcW;
      if (cols == 0)
        cols = 1;

      int col = frameIndex % cols;
      int row = frameIndex / cols;

      srcX = col * srcW;
      srcY = row * srcH;
    }
    // full texture
    else if (def.useFullTexture) {
      srcX = 0;
      srcY = 0;
      srcW = texture.getWidth();
      srcH = texture.getHeight();
    }
    // manual crop
    else {
      srcX = def.x;
      srcY = def.y;
      srcW = def.width;
      srcH = def.height;
    }

    batch.draw(
        texture,
        worldX, worldY,
        width, height,
        srcX, srcY,
        srcW, srcH,
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
  public void drawCircle(float x, float y, float radius, float r, float g, float b, float a) {
    shapeRenderer.set(ShapeType.Line);
    shapeRenderer.setColor(r, g, b, a);
    shapeRenderer.circle(x, y, radius);
  }

  @Override
  public void drawFilledCircle(float x, float y, float radius, float r, float g, float b, float a) {
    shapeRenderer.set(ShapeType.Filled);
    shapeRenderer.setColor(r, g, b, a);
    shapeRenderer.circle(x, y, radius);
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

  public void drawProgressbar(float x, float y, int width, int height, float progress, float r1, float g1, float b1,
      float a1, float r2, float g2, float b2, float a2) {
    shapeRenderer.setColor(r1, g1, b1, a1);
    shapeRenderer.rect(x, y, width, height);

    shapeRenderer.setColor(r2, g2, b2, a2);
    shapeRenderer.rect(x, y, width * progress, height);
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
