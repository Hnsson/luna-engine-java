package com.engine.gdx.io;

import com.badlogic.gdx.Gdx;
import com.engine.FileContext;

public class GDXFileHandler implements FileContext {
  @Override
  public String readFile(String path) {
    return Gdx.files.local(path).readString();
  }

  @Override
  public void writeFile(String path, String content) {
    Gdx.files.local(path).writeString(content, false);
  }
}
