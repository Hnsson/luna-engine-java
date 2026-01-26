package com.engine.rendering.contexts;

/*
 * Defines the loading and managing assets (Textures, Sounds, etc.)
 * in a framework-specific way (LibGDX, OpenGL, ...).
 * The core rendering / sprite(definition + component) only uses logical paths (Strings).
 * It cannot know about framework-specific objects like LibGDX's 'Texture'
 * or OpenGL's 'int' handles.
 *
 * The concrete implementation should load the file from disk. Mapping the generic
 * file path (e.g., "textures/atlas.png") to the actual object, provide that object
 * to the specific RenderContext.java when requested.
 */
public interface IAssetManager {

  void loadAllTextures(String internalPath);

  void loadTexture(String path);

  void registerTexture(String spriteId, String filePath);

  void unloadTexture(String path);
}
