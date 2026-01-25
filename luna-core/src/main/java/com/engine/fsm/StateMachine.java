package com.engine.fsm;

import java.util.Stack;

import com.engine.fsm.states.Layer;

public class StateMachine<T extends Layer> {
  private Stack<T> layers = new Stack<>();

  public void pushLayer(T layer) {
    layers.push(layer);
    layer.enter();
  }

  public T popLayer() {
    if (layers.isEmpty())
      return null;
    T layer = layers.pop();
    layer.exit();
    return layer;
  }

  public void setLayer(T layer) {
    layers.clear();
    layers.push(layer);
    layer.enter();
  }

  public T getCurrentState() {
    if (layers.isEmpty())
      return null;
    return layers.peek();
  }

  public boolean isEmpty() {
    return layers.isEmpty();
  }
}
