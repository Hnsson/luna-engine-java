package com.engine.ecs;

import java.util.HashMap;
import java.util.Map;

public class ComponentRegistry {
  private static int lastID = 0;

  private static Map<Class<? extends Component>, Integer> componentTypes = new HashMap<>();

  public static <T extends Component> int getComponentTypeID(Class<T> componentClass) {
    if (!componentTypes.containsKey(componentClass)) {
      componentTypes.put(componentClass, lastID++);
      System.out.println("Registered Component: " + componentClass.getSimpleName());
    }
    return componentTypes.get(componentClass);
  }

  public static void clear() {
    componentTypes.clear();
    lastID = 0;
  }
}
