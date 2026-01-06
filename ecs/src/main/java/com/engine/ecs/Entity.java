package com.engine.ecs;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Entity {
  private byte maxComponents = 32;
  private String startDialogueNodeId;
  private Integer id;
  private List<Component> components = new ArrayList<>();
  private BitSet activeComponents;

  public Entity() {
    this.id = 0;
    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
  }

  public Entity(Integer id) {
    this.id = id;
    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
  }

  public Entity(String startDialogueNodeId) {
    this.id = 0;
    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
    this.startDialogueNodeId = startDialogueNodeId;
  }

  public int getID() {
    return this.id;
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> T addComponent(T component) {
    int typeID = ComponentRegistry.getComponentTypeID(component.getClass());

    if (typeID >= this.maxComponents) {
      throw new RuntimeException("Exceeded max component limit (" + String.valueOf(this.maxComponents) + ")");
    }

    if (this.hasComponent((Class<T>) component.getClass())) {
      return getComponent((Class<T>) component.getClass());
    }

    this.components.add(component);
    this.activeComponents.set(typeID);

    System.out.println("Added component (" + component.getClass().getSimpleName() + ")");

    return component;
  }

  public <T extends Component> boolean hasComponent(Class<T> componentClass) {
    int typeID = ComponentRegistry.getComponentTypeID(componentClass);
    return this.activeComponents.get(typeID);
  }

  public <T extends Component> T getComponent(Class<T> componentClass) {
    for (Object c : this.components) {
      if (componentClass.isInstance(c)) {
        return componentClass.cast(c);
      }
    }
    return null;
  }

  public String startDialogueNodeId() {
    return this.startDialogueNodeId;
  }

  public void init() {
  }

  public void eventHandler() {
  }

  public void update() {
    for (Component cmp : this.components) {
      cmp.update();
    }
  }

  public void render() {
  }
}
