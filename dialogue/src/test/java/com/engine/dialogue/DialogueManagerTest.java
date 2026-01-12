package com.engine.dialogue;

import com.engine.dialogue.graph.DialogueGraph;
import com.engine.dialogue.graph.DialogueNode;
import com.engine.dialogue.graph.DialogueOption;
import com.engine.dialogue.logic.ConditionRegistry;
import com.engine.ecs.Entity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class DialogueManagerTest {

  private DialogueManager manager;
  private Entity player;

  @BeforeEach
  void setUp() {
    manager = new DialogueManager();
    player = new Entity();
  }

  @Test
  void testSimpleNavigation() {
    DialogueOption opt1 = new DialogueOption("Go to end", "node_end", "", false);

    DialogueNode startNode = new DialogueNode("Hello!", List.of(opt1));
    DialogueNode endNode = new DialogueNode("Bye!", List.of());

    Map<String, DialogueNode> nodes = new HashMap<>();
    nodes.put("node_start", startNode);
    nodes.put("node_end", endNode);

    DialogueGraph graph = new DialogueGraph("node_start", nodes);
    manager.addGraph(graph);

    manager.startDialogue("node_start", player);
    assertTrue(manager.isActive());

    manager.chooseOption(1);
    // Should have jumped to end node (which has no options so is still active)
    assertTrue(manager.isActive());
  }

  @Test
  void testConditionFiltering() throws Exception {
    ConditionRegistry.register("is_warrior", ctx -> false);

    // Requires warrior (Should be hidden because condition returns false)
    DialogueOption opt = new DialogueOption("Use heavy sword", "node_attack", "is_warrior", false);
    // Valid options
    DialogueOption opt2 = new DialogueOption("Run away", "node_flee", "", false);

    DialogueNode startNode = new DialogueNode("Ketheric is in your way", List.of(opt, opt2));

    Map<String, DialogueNode> nodes = new HashMap<>();
    nodes.put("start", startNode);

    DialogueGraph graph = new DialogueGraph("start", nodes);
    manager.addGraph(graph);
    manager.startDialogue("start", player);

    List<DialogueOption> visibleOptions = manager.getValidOptions();

    assertEquals(1, visibleOptions.size(), "Should hide the warrior option and show the other option");
    assertEquals("Run away", visibleOptions.get(0).getText());
  }

  @Test
  void testEndConversationOption() throws Exception {
    ConditionRegistry.register("is_warrior", ctx -> false);

    DialogueOption opt = new DialogueOption("End conversation", "", "", true);
    DialogueNode startNode = new DialogueNode("Bye!", List.of(opt));

    Map<String, DialogueNode> nodes = new HashMap<>();
    nodes.put("start", startNode);

    DialogueGraph graph = new DialogueGraph("start", nodes);
    manager.addGraph(graph);
    manager.startDialogue("start", player);

    manager.chooseOption(1);

    assertFalse(manager.isActive(), "Dialogue should end when option with end=true is chosen");
  }
}
