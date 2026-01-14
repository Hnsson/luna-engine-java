package com.game;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;

public class DesktopLauncher {
  public static final int WINDOW_WIDTH = 800;
  public static final int WINDOW_HEIGHT = 600;

  public static void main(String[] args) {
    Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();

    config.setTitle("Luna Engine Sandbox");
    config.setWindowedMode(WINDOW_WIDTH, WINDOW_HEIGHT);

    new Lwjgl3Application(new Sandbox(), config);
  }
}
