package com.engine.gdx;

import com.engine.ecs.Component;
import com.engine.ecs.Entity;

public abstract class Script extends Component {
  public abstract void onTriggerEnter(Entity entity);

  public abstract void onTriggerLeave(Entity entity);
}
