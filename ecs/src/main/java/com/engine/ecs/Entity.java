package com.engine.ecs;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Entity {
  private static int nextId = 0;

  private String name;
  private final int id;
  private String startDialogueNodeId;

  private byte maxComponents = 32;
  private List<Component> components = new ArrayList<>();
  private BitSet activeComponents;

  public Entity() {
    this.id = nextId++;
    this.name = "Entity_" + this.id;
    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
  }

  public Entity(String name) {
    this.id = nextId++;
    if (name == null) {
      this.name = "Entity_" + this.id;
    } else {
      this.name = name;
    }
    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
  }

  public Entity(String name, String startDialogueNodeId) {
    this.id = nextId++;
    if (name == null) {
      this.name = "Entity_" + this.id;
    } else {
      this.name = name;
    }
    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
    this.startDialogueNodeId = startDialogueNodeId;
  }

  public int getId() {
    return this.id;
  }

  public String getName() {
    return this.name;
  }

  public String getDialogueNodeId() {
    return this.startDialogueNodeId;
  }

  public static void clear() {
    nextId = 0;
  }

  @SuppressWarnings("unchecked")
  public <T extends Component> T addComponent(T component) {
    int typeId = ComponentRegistry.getComponentTypeID(component.getClass());

    if (typeId >= this.maxComponents) {
      throw new RuntimeException("Exceeded max component limit (" + String.valueOf(this.maxComponents) + ")");
    }

    if (this.hasComponent((Class<T>) component.getClass())) {
      return getComponent((Class<T>) component.getClass());
    }

    this.components.add(component);
    this.activeComponents.set(typeId);

    System.out.println("Added component (" + component.getClass().getSimpleName() + ")");

    return component;
  }

  public <T extends Component> boolean hasComponent(Class<T> componentClass) {
    int typeId = ComponentRegistry.getComponentTypeID(componentClass);
    return this.activeComponents.get(typeId);
  }

  public <T extends Component> T getComponent(Class<T> componentClass) {
    for (Object c : this.components) {
      if (componentClass.isInstance(c)) {
        return componentClass.cast(c);
      }
    }
    return null;
  }

  public void init() {
  }

  public void eventHandler() {
    for (Component cmp : this.components) {
      cmp.eventHandler();
    }
  }

  public void update() {
    for (Component cmp : this.components) {
      cmp.update();
    }
  }

  public void render() {
    for (Component cmp : this.components) {
      cmp.render();
    }
  }
}
