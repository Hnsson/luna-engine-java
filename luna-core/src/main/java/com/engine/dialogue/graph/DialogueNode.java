package com.engine.dialogue.graph;

import java.util.List;

public class DialogueNode {
  private String text;
  private List<DialogueOption> options;

  public DialogueNode() {
  }

  /*
   * Defined for easier testing
   */
  public DialogueNode(String text, List<DialogueOption> options) {
    this.text = text;
    this.options = options;
  }

  public String getText() {
    return this.text;
  }

  public List<DialogueOption> getOptions() {
    return this.options;
  }
}
