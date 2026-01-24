package com.engine.gdx.rendering;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.engine.rendering.contexts.IAssetManager;

public class GDXAssetManager implements IAssetManager, Disposable {
  private final AssetManager manager;

  public GDXAssetManager() {
    this.manager = new AssetManager();
  }

  @Override
  public void loadTexture(String path) {
    manager.load(path, Texture.class);
    manager.finishLoading();
  }

  @Override
  public void unloadTexture(String path) {
    if (manager.isLoaded(path)) {
      manager.unload(path);
    }
  }

  public Texture get(String path) {
    if (!manager.isLoaded(path)) {
      return null;
    }
    return manager.get(path, Texture.class);
  }

  @Override
  public void dispose() {
    manager.dispose();
  }
}
