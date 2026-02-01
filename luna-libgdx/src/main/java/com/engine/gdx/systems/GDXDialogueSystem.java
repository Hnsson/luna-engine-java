package com.engine.gdx.systems;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.engine.GameSystem;
import com.engine.dialogue.DialogueManager;
import com.engine.ecs.Entity;
import com.engine.dialogue.graph.DialogueOption;
import com.engine.gdx.rendering.GDXRender;

public class GDXDialogueSystem implements GameSystem {
  private String levelName;

  private DialogueManager manager;
  private GDXRender renderer;
  private GDXInputSystem input;

  private Entity target;
  private Entity initiator;

  private int currentSelectionIndex = 0;

  private float stringCompleteness = 0;
  private float textSpeed = 20;
  private boolean textFinished = false;

  public GDXDialogueSystem(String levelName, DialogueManager manager, GDXRender renderer, GDXInputSystem input) {
    this.levelName = levelName;
    this.manager = manager;
    this.renderer = renderer;
    this.input = input;

    manager.loadAllGraphs("/graphs/" + levelName);
  }

  public boolean isActive() {
    return manager.isActive();
  }

  public void startDialogue(Entity target, Entity initiator) {
    this.initiator = initiator;
    this.target = target;
    manager.startDialogue(target.getDialogueNodeId(), initiator);
  }

  @Override
  public void eventHandler() {
    if (!manager.isActive())
      return;

    if (Gdx.input.isKeyJustPressed(input.getKeybind(GDXInputSystem.Mapping.UP))) {
      currentSelectionIndex--;
      if (currentSelectionIndex < 0) {
        currentSelectionIndex = manager.getValidOptions().size() - 1;
      }
    }

    if (Gdx.input.isKeyJustPressed(input.getKeybind(GDXInputSystem.Mapping.DOWN))) {
      currentSelectionIndex++;
      if (currentSelectionIndex >= manager.getValidOptions().size()) {
        currentSelectionIndex = 0;
      }
    }

    if (Gdx.input.isKeyJustPressed(input.getKeybind(GDXInputSystem.Mapping.ENTER))) {
      if (textFinished) {
        manager.chooseOption(currentSelectionIndex);
        currentSelectionIndex = 0;
        stringCompleteness = 0;
        textFinished = false;
      }
    }
  }

  @Override
  public void render() {
    if (!manager.isActive())
      return;

    renderer.beginUIShapeFrame();
    renderer.drawFilledRectangle(50, 10, 700, 200, 0, 0, 0, 0.8f);
    renderer.endUIShapeFrame();

    renderer.beginUIFrame();
    renderer.drawText(target.getName(), 60, 180, 1, 1, 0, 1);

    // This typewritter effect worked great
    // (https://gamedev.stackexchange.com/questions/90182/typing-text-animation-dialogue-libgdx-or-in-general)
    int charCountThisFrame = (int) stringCompleteness;
    if (charCountThisFrame > manager.getCurrentNodeText().length()) {
      charCountThisFrame = manager.getCurrentNodeText().length();
      textFinished = true;
    }
    renderer.drawText(manager.getCurrentNodeText().substring(0, charCountThisFrame), 60, 160, 1, 1, 1, 1);

    List<DialogueOption> options = manager.getValidOptions();
    float optionStartY = 120;
    for (int i = 0; i < options.size(); i++) {
      DialogueOption opt = options.get(i);
      float y = optionStartY - (i * 20);

      if (textFinished) {
        if (i == currentSelectionIndex) {
          renderer.drawText("> " + opt.getText(), 60, y, 0, 1, 0, 1);
        } else {
          renderer.drawText(opt.getText(), 80, y, 0.7f, 0.7f, 0.7f, 1);
        }
      }
    }

    renderer.endUIFrame();
  }

  @Override
  public void update(float delta) {
    if (!manager.isActive())
      return;

    stringCompleteness += textSpeed * delta;
  }
}
