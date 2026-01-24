package com.engine.ecs.components.logic;

import com.engine.ecs.Component;
import com.engine.math.Vector2f;

/*
 * This component should be the movement of the entity. It should be a pure data class
 * cause I want serialization so it only contains numbers (easy to save inputs per frame in JSON)
 * So this logic module should not need to know about the movement system which should be implemented
 * per framework (LibGDX, OpenGL, ...), so this component remains unchanged
 */
public class PlayerController extends Component {
  public Vector2f moveDir = new Vector2f(0, 0);
}
