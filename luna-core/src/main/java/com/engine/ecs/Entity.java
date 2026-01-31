package com.engine.ecs;

import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

import com.engine.rendering.RenderLayerRegistry.Layers;

public class Entity {
  private static int nextId = 0;

  private String name;
  private final int id;
  private String startDialogueNodeId;

  private byte maxComponents = 32;
  private List<Component> components = new ArrayList<>();
  private transient BitSet activeComponents;
  private Layers layer = Layers.DEFAULT;

  public Entity() {
    this(null);
  }

  public Entity(String name) {
    this(name, null);
  }

  public Entity(String name, String startDialogueNodeId) {
    this(name, startDialogueNodeId, null);
  }

  public Entity(String name, String startDialogueNodeId, Layers layer) {
    this.id = nextId++;

    if (name == null) {
      this.name = "Entity_" + this.id;
    } else {
      this.name = name;
    }

    this.components = new ArrayList<>();
    this.activeComponents = new BitSet(this.maxComponents);
    this.startDialogueNodeId = startDialogueNodeId;
    this.layer = layer;
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

  public Layers getLayer() {
    return this.layer;
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

    component.entity = this;
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

  public List<Component> getComponents() {
    return this.components;
  }

  public void init() {
    // Moved the initalization from JSON to objects for the components here so its
    // done by the entity instead of in the serializer because it was messy code in
    // the serializer which feels more logical to be here:
    if (this.activeComponents == null) {
      this.activeComponents = new BitSet(this.maxComponents);
    }

    if (this.id >= nextId) {
      nextId = this.id + 1;
    }

    for (Component cmp : this.components) {
      cmp.entity = this;

      int typeId = ComponentRegistry.getComponentTypeID(cmp.getClass());
      this.activeComponents.set(typeId);

      cmp.init();
    }
  }
}
