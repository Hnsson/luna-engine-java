package com.engine.dialogue.graph;

public class DialogueOption {
  private String text;
  private String targetId;
  private String conditionId;
  private Boolean end;

  public DialogueOption() {
  }

  /*
   * Defined for easier testing
   */
  public DialogueOption(String text, String targetId, String conditionId, Boolean end) {
    this.text = text;
    this.targetId = targetId;
    this.conditionId = conditionId;
    this.end = end;
  }

  public String getText() {
    return this.text;
  }

  public String getTargetId() {
    return this.targetId;
  }

  public String conditionId() {
    return this.conditionId;
  }

  public Boolean getEnd() {
    return this.end;
  }
}
