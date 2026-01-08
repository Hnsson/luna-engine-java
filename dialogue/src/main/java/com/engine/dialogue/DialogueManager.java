package com.engine.dialogue;

import com.engine.dialogue.graph.DialogueGraph;
import com.engine.dialogue.graph.DialogueNode;
import com.engine.dialogue.graph.DialogueOption;
import com.engine.dialogue.io.DialogueLoader;
import com.engine.dialogue.logic.Condition;
import com.engine.dialogue.logic.ConditionRegistry;
import com.engine.dialogue.logic.DialogueContext;
import com.engine.ecs.Entity;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class DialogueManager {
  private Map<String, DialogueGraph> graphStore;

  private DialogueGraph activeGraph;
  private DialogueNode activeNode;

  // Set the entity that is currently talking to NPC to get THEIR context.
  private Entity currentUser;
  private Map<String, Object> globalFlags;

  // Want a validOptions list to dispaly because not all options should be
  // available to the user (If warrior show one, if rogue show another)
  private List<DialogueOption> validOptions;

  public DialogueManager() {
    this.graphStore = new HashMap<>();
    this.globalFlags = new HashMap<>();

    System.out.println("VAAAAAAAA?");
    ConditionRegistry.init();
    // registerGraph("/graphs/blacksmith_dialogue.json");
  }

  public void loadAllGraphs(String folderPath) {
    try {
      URL url = getClass().getResource(folderPath);
      if (url == null) {
        System.err.println("[ERROR::DIALOGUEMANAGER] Folder not found (" + folderPath + ")");
        return;
      }

      Path dirPath = Paths.get(url.toURI());
      try (Stream<Path> paths = Files.walk(dirPath)) {
        paths.filter(Files::isRegularFile).filter(p -> p.toString().endsWith(".json")).forEach(filePath -> {
          String filename = filePath.getFileName().toString();
          String cleanFolderPath = folderPath.endsWith("/") ? folderPath : folderPath + "/";
          String resourcePath = cleanFolderPath + filename;

          registerGraph(resourcePath);
        });
      }
    } catch (IOException | URISyntaxException e) {
      e.printStackTrace();
    }
  }

  public void registerGraph(String path) {
    DialogueGraph graph = DialogueLoader.load(path);
    if (graph != null) {
      graphStore.put(graph.getStartNode(), graph);
      System.out.println("Loaded Graph entry point: " + graph.getStartNode());
    }
  }

  public void startDialogue(String startNodeId, Entity player) {
    DialogueGraph graph = graphStore.get(startNodeId);
    if (graph == null) {
      System.err.println("Error: No graph found starting with ID: " + startNodeId);
      return;
    }

    this.currentUser = player;
    this.activeGraph = graph;

    jumpToNode(startNodeId);
  }

  private void jumpToNode(String nodeId) {
    DialogueNode node = activeGraph.getNodes().get(nodeId);

    if (node == null) {
      System.err.println("Error: Node not found " + nodeId);

      // End Conversation
      endConversation();
      return;
    }

    this.activeNode = node;
    updateValidOptions();

    printCurrentState();
  }

  private void updateValidOptions() {
    this.validOptions = new ArrayList<>();

    DialogueContext context = new DialogueContext(this.currentUser, this.globalFlags);

    for (DialogueOption option : activeNode.getOptions()) {
      /*
       * Retrieve the Condition from the speficied label which has been created in the
       * map with that specific label definition to isMet.
       */
      Condition condition = ConditionRegistry.get(option.conditionId());

      /*
       * This is where we call the actual function which has that specific label
       * definition to isMet we want and call it with our context.
       */
      if (condition.isMet(context)) {
        validOptions.add(option);
      }
    }
  }

  public void chooseOption(int optionIndex) {
    // Options like (0, 1, 2, ...)
    if (!isActive())
      return;

    int internalIndex = optionIndex - 1;

    List<DialogueOption> options = activeNode.getOptions();

    if (internalIndex < 0 || internalIndex >= validOptions.size()) {
      System.out.println("Invalid choice.");
      return;
    }

    DialogueOption selected = validOptions.get(internalIndex);

    if (Boolean.TRUE.equals(selected.getEnd())) {
      endConversation();
    } else {
      jumpToNode(selected.getTargetId());
    }
  }

  private void endConversation() {
    System.out.println("[End of Conversation]");
    this.activeNode = null;
    this.activeGraph = null;
    this.currentUser = null;
  }

  private void printCurrentState() {
    if (activeNode == null)
      return;

    System.out.println("--------------------------------");
    System.out.println("NPC: " + activeNode.getText());
    System.out.println("--------------------------------");

    for (int i = 0; i < validOptions.size(); i++) {
      DialogueOption opt = validOptions.get(i);
      System.out.println("[" + (i + 1) + "] " + opt.getText());
    }
    System.out.println("--------------------------------");
  }

  public boolean isActive() {
    return activeNode != null;
  }
}
