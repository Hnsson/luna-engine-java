package com.mapbuilder;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
  public static final int WINDOW_WIDTH = 1280;
  public static final int WINDOW_HEIGHT = 720;

  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

    config.setTitle("Luna Engine MapBuilder");
    config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

    new Lwjgl3Application(new MapBuilder(), config);
  }
}
