package com.engine.dialogue.graph;

import java.util.Map;
import com.engine.dialogue.graph.DialogueNode;

public class DialogueGraph {
  private String startNode;
  private Map<String, DialogueNode> nodes;

  public DialogueGraph() {}

  public String getStartNode() {
    return this.startNode;
  }

  public Map<String, DialogueNode> getNodes() {
    return this.nodes;
  }
}
