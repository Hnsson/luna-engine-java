package com.engine.rendering.contexts;

import com.engine.rendering.models.SpriteDefinition;

/*
 * Defines the drawing operations on the screen in a framework-specific way.
 * The core rendering / sprite(definition + component) cannot draw itself because
 * it only holds logical paths and definitions. It should be general to be
 * used as general (not implemented multiple times) when creating multiple different
 * games using different frameworks.
 *
 * The concrete implementation (definition of interface) is should do the actual
 * draw calls to the graphics card (framework-specific).
 */
public interface IRenderContext {
  void clearScreen(float r, float g, float b);

  void beginFrame();

  void endFrame();

  void beginDebugFrame();

  void endDebugFrame();

  void beginUIFrame();

  void endUIFrame();

  void beginUIShapeFrame();

  void endUIShapeFrame();

  void resize(int width, int height);

  void drawSprite(SpriteDefinition def, float x, float y, float width, float height, boolean flipX, boolean flipY);

  void drawRectangle(float x, float y, float width, float height, float r, float g, float b, float a);

  void drawFilledRectangle(float x, float y, float width, float height, float r, float g, float b, float a);

  float getTextWidth(String text);

  void drawText(String text, float x, float y, float r, float g, float b, float a);

  void drawProgressbar(float x, float y, int width, int height, float progress, float r1, float g1, float b1,
      float a1, float r2, float g2, float b2, float a2);
}
