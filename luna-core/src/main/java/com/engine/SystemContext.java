package com.engine;

import com.engine.ecs.EntityManager;

// easy interface to hold core-only systems, right now only EntityManager is needed, but is mostly
// needed to be extended by ther script context.
public interface SystemContext {
  EntityManager getEntityManager();
}
