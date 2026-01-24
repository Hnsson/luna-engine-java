package com.engine.math;

/*
 * Wanted to try out adding a pseudo-random library after checking out
 * the Linear Congrumential Generator
 * https://rosettacode.org/wiki/Linear_congruential_generator#Java
 */
public class Random {
  // Want it to be called more neatly like Random.Int() or Random.Float()
  // instead of having to create a object
  private static final Random global = new Random();
  private long seed;

  private final static int mask = (1 << 31) - 1;
  private final static int multiplier = 214_013;
  private final static int increment = 2_531_011;

  public Random() {
    this(System.currentTimeMillis());
  }

  public Random(long seed) {
    this.seed = seed;
  }

  /*
   * Returns random integer between min-max (not including max)
   */
  public static int Int(int min, int max) {
    return global.RndInteger(min, max);
  }

  public int RndInteger(int min, int max) {
    if (min >= max) {
      throw new IllegalArgumentException("Max must be greater than min");
    }

    seed = (seed * multiplier + increment) & mask;
    long random = seed >> 16;

    return min + (int) (random % (max - min));
  }
}
