package com.engine.dialogue.graph;

import java.util.Map;

public class DialogueGraph {
  private String startNode;
  private Map<String, DialogueNode> nodes;

  public DialogueGraph() {
  }

  /*
   * Defined for easier testing
   */
  public DialogueGraph(String startNode, Map<String, DialogueNode> nodes) {
    this.startNode = startNode;
    this.nodes = nodes;
  }

  public String getStartNode() {
    return this.startNode;
  }

  public Map<String, DialogueNode> getNodes() {
    return this.nodes;
  }
}
