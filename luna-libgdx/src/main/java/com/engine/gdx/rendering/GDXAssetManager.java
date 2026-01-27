package com.engine.gdx.rendering;

import java.util.ArrayList;
import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Disposable;
import com.engine.rendering.contexts.IAssetManager;
import com.engine.rendering.logic.SpriteRegistry;
import com.engine.rendering.models.SpriteDefinition;

public class GDXAssetManager implements IAssetManager, Disposable {
  private final AssetManager manager = new AssetManager();
  // Want to have a cool progress bar when loading textures, will have a list of
  // files so we know how many's left
  private final List<FileHandle> files = new ArrayList<>();
  private String loadingRootPath = "";

  @Override
  public void loadAllTextures(String internalPath) {
    this.loadingRootPath = internalPath;
    FileHandle root = Gdx.files.internal(internalPath);

    if (!root.exists()) {
      System.err.println("[ERROR::GDXASSETMANAGER] Asset path not found: " + internalPath);
      return;
    }
    files.clear();
    processFile(root);
  }

  private void processFile(FileHandle file) {
    if (file.isDirectory()) {
      for (FileHandle child : file.list())
        processFile(child);
    } else if (file.extension().equalsIgnoreCase("png")) {
      loadTexture(file.path());
      files.add(file);
    }
  }

  public void registerAllTextures() {
    for (FileHandle file : files) {
      String spriteId = generateSpriteId(file.pathWithoutExtension());

      registerTexture(spriteId, file.path());
    }
    files.clear();
    loadingRootPath = "";
  }

  private String generateSpriteId(String path) {
    String relative = path;

    if (path.startsWith(loadingRootPath)) {
      int offset = loadingRootPath.length();
      // remember to check for windows path (\\)
      if (path.length() > offset &&
          (path.charAt(offset) == '/' || path.charAt(offset) == '\\')) {
        offset++;
      }
      relative = path.substring(offset);
    }

    // Want this [\/\\] from (https://regex101.com/)
    // Needed to use double \\ to achieve \ in string
    return relative.replaceAll("[/\\\\]", "_");
  }

  public boolean updateLoading() {
    return manager.update();
  }

  public float getProgress() {
    return manager.getProgress();
  }

  public void finishLoading() {
    manager.finishLoading();
  }

  @Override
  public void loadTexture(String path) {
    manager.load(path, Texture.class);
  }

  @Override
  public void registerTexture(String spriteId, String filePath) {
    // Will need to find a good way to handle spritesheet, now I just hard coded
    // width and height for sprite in sprite sheet because I know the sprites are
    // that width and height, but has to find a better way.
    // Texture tex = manager.get(filePath, Texture.class);
    SpriteRegistry.register(spriteId, new SpriteDefinition(filePath, 0, 0, 192, 192));
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

  // [NOTE FOR SELF]: probably don't dispose manager like this, because this is
  // called when a GameLayer is exiting which means that every level switch it
  // will remove not only the level specific assets (which it should) it also
  // unloads all the common assets (which it SHOULDN'T), so probably create custom
  // unload function for all assets except common, then call that when switching
  // level (in the exit() function in GameLayer)
  @Override
  public void dispose() {
    manager.dispose();
  }
}
