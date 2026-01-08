package com.engine.dialogue.logic;

import com.engine.ecs.Entity;
import java.util.Map;

public class DialogueContext {
  // The context provided to decide the condition (true or false)
  public Entity player; // To get player components (if inventory_gold > 10 --> return true)
  public Map<String, Object> globalFlags; // For global player flags (if boss_1_killed == true --> return true)

  public DialogueContext(Entity player, Map<String, Object> flags) {
    this.player = player;
    this.globalFlags = flags;
  }
}
