package com.engine;

import com.engine.ecs.Component;
import com.engine.ecs.Entity;

// I feel like this is where I need to diverge from normal pure ECS behaviour and allow the components or in this case scripts to be logic 
public abstract class Script extends Component {
  public abstract void onTriggerEnter(Entity entity);

  public abstract void onTriggerLeave(Entity entity);

  public abstract void update(float delta, SystemContext context);

  public abstract void render(SystemContext context);

  public abstract void eventHandler(SystemContext context);
}
