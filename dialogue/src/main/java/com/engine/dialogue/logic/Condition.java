package com.engine.dialogue.logic;
// https://www.geeksforgeeks.org/java/java-functional-interfaces/ (helped)

/* This is a Functional Interface which is why it has one abstract method (not yet defined).
 * This allows us to use Lambda syntax (ctx -> ...) instead of creating new classes (polymorphims) to define the abstract method.
 * So this is how I understand it and the starting point for the logic:
 * (1) [Condition] - You have a abstract interface with only a definition and a parameter and let's lambda functions define it.
 * (2) [ConditionRegister] - Here you actually take a abstract interface condition and give isMet (only definition) an actual implementation
 * (3) [DialogueManager] - And here you fetch the Condition with the specific implementation you want and provide it with context to get the answer from it (true or false)
*/
public interface Condition {
  /*
   * Only a definition and a parameter (The Context) needed for the logic to be
   * defined and returned.
   */
  boolean isMet(DialogueContext context);
}
