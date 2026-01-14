package com.engine.dialogue.logic;

import java.util.HashMap;
import java.util.Map;

import com.engine.ecs.components.Transform;

public class ConditionRegistry {
  private static final Map<String, Condition> conditions = new HashMap<>();

  /*
   * This is where conditions are created and actually defined
   * with their parameters (Context) using lambda functions.
   */
  static {
    register("always_true", ctx -> true);
    register("is_warrior", ctx -> {
      return true;
    });
    register("has_gold_10", ctx -> {
      // Get like player inventory component and check if inventory.gold >= 10;
      return false;
    });
    register("is_running", ctx -> {
      return Math.abs(ctx.player.getComponent(Transform.class).velocity.x) > 0;
    });
  }

  public static void register(String id, Condition condition) {
    conditions.put(id, condition);
  }

  public static Condition get(String id) {
    return conditions.getOrDefault(id, ctx -> true);
  }
}
