package com.engine.dialogue;

import com.engine.dialogue.graph.DialogueGraph;
import com.engine.dialogue.graph.DialogueNode;
import com.engine.dialogue.graph.DialogueOption;
import com.engine.dialogue.io.DialogueLoader;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DialogueManager {
  private Map<String, DialogueGraph> graphStore;

  private DialogueGraph activeGraph;
  private DialogueNode activeNode;

  public DialogueManager() {
    this.graphStore = new HashMap<>();

    // registerGraph("/graphs/blacksmith_dialogue.json");
  }

  public void registerGraph(String path) {
    DialogueGraph graph = DialogueLoader.load(path);
    if (graph != null) {
        graphStore.put(graph.getStartNode(), graph);
        System.out.println("Loaded Graph entry point: " + graph.getStartNode());
    }
  }

  public void startDialogue(String startNodeId) {
    DialogueGraph graph = graphStore.get(startNodeId);
    if (graph == null) {
        System.err.println("Error: No graph found starting with ID: " + startNodeId);
        return;
    }

    this.activeGraph = graph;
    jumpToNode(startNodeId);
  }

  private void jumpToNode(String nodeId) {
    DialogueNode node = activeGraph.getNodes().get(nodeId);

    if (node == null) {
      System.err.println("Error: Node not found " + nodeId);

      // End Conversation
      this.activeNode = null;
      this.activeGraph = null;
      return;
    }

    this.activeNode = node;
    printCurrentState();
  }

  public void chooseOption(int optionIndex) {
    // Options like (0, 1, 2, ...)
    if (activeNode == null || activeNode.getOptions() == null) return;

    List<DialogueOption> options = activeNode.getOptions();

    if (optionIndex < 0 || optionIndex >= options.size()) {
      System.out.println("Invalid choice.");
      return;
    }

    DialogueOption selected = options.get(optionIndex);

    if (Boolean.TRUE.equals(selected.getEnd())) {
      System.out.println("[End of Conversation]");
      this.activeNode = null;
      this.activeGraph = null;
    } else {
      jumpToNode(selected.getTargetId());
    }
  }

  private void printCurrentState() {
    if (activeNode == null) return;

    System.out.println("--------------------------------");
    System.out.println("NPC: " + activeNode.getText());
    System.out.println("--------------------------------");

    List<DialogueOption> options = activeNode.getOptions();
    for (int i = 0; i < options.size(); i++) {
        DialogueOption opt = options.get(i);
        // In the future, check Conditions here!
        System.out.println("[" + i + "] " + opt.getText());
    }
    System.out.println("--------------------------------");
  }

  public boolean isActive() {
    return activeNode != null;
  }
}
