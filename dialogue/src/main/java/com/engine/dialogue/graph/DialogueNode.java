package com.engine.dialogue.graph;

import com.engine.dialogue.graph.DialogueOption;
import java.util.List;

public class DialogueNode {
  private String text;
  private List<DialogueOption> options;

  public DialogueNode() {}

  public String getText() {
    return this.text;
  }

  public List<DialogueOption> getOptions() {
    return this.options;
  }
}

