package com.engine.level;

import java.util.List;

import com.engine.FileContext;
import com.engine.ecs.ECSSerializer;
import com.engine.ecs.Entity;
import com.engine.ecs.EntityManager;
import com.engine.ecs.components.Transform;
import com.engine.ecs.components.physics.BoxCollider;
import com.engine.ecs.components.physics.RigidBody;
import com.engine.level.LevelData.LayerData;
import com.engine.level.map.LevelMap;
import com.engine.rendering.RenderLayerRegistry.Layers;

public class LevelManager {
  private EntityManager entityManager;
  private ECSSerializer serializer;
  private FileContext fileHandler;
  private LevelMap levelMap;
  private WorldLoader worldLoader;

  public LevelManager(String levelName, FileContext fileHandler) {
    this.entityManager = new EntityManager();
    this.serializer = new ECSSerializer();
    this.worldLoader = new WorldLoader();
    this.fileHandler = fileHandler;
  }

  public EntityManager getEntityManager() {
    return entityManager;
  }

  public LevelMap getLevelMap() {
    return levelMap;
  }

  public Entity loadLevel(String levelName) {
    entityManager.clear();

    String json = fileHandler.readFile("levels/" + levelName + ".json");
    LevelData data = serializer.loadLevel(json);
    if (data == null || data.entities == null)
      return null;

    // Load world
    this.levelMap = worldLoader.loadMap(data);
    loadCollisionLayer(data);

    // Load entities
    String savePath = getSavePath(levelName);
    if (fileHandler.exists(savePath)) {
      // Save already found for this level
      entityManager.loadEntities(savePath, fileHandler, serializer, true);
    } else {
      // new game
      for (Entity entity : data.entities) {
        entityManager.addEntity(entity);
      }
    }

    String savePlayerPath = getPlayerPath();
    if (fileHandler.exists(savePlayerPath)) {
      return entityManager.loadEntity(savePlayerPath, fileHandler, serializer, false);
    }
    // no player exists
    return null;
  }

  public void loadCollisionLayer(LevelData data) {
    if (data.tilemap.layers == null)
      return;

    for (LayerData layer : data.tilemap.layers) {
      if (!layer.name.equals("colliders")) {
        continue;
      }

      int rowCount = layer.data.size();
      for (int i = 0; i < rowCount; i++) {
        List<Integer> row = layer.data.get(i);

        for (int j = 0; j < row.size(); j++) {
          int tileId = row.get(j);
          if (tileId == 0)
            continue;

          float worldX = j * layer.tileSize;
          float worldY = (rowCount - 1 - i) * layer.tileSize;
          createColliderEntity(worldX, worldY, layer.tileSize, layer.layer);
        }
      }
    }
  }

  private void createColliderEntity(float x, float y, int size, Layers layer) {
    Entity wall = new Entity();
    wall.setLayer(layer);

    Transform t = new Transform(x, y);
    BoxCollider collider = new BoxCollider(size, size);
    RigidBody rb = new RigidBody();
    rb.immovable = true;

    wall.addComponent(t);
    wall.addComponent(collider);
    wall.addComponent(rb);

    entityManager.addEntity(wall);
  }

  public void saveGame(String levelName, Entity player) {
    entityManager.saveEntities(getSavePath(levelName), fileHandler, serializer, player);
    entityManager.saveEntity(getPlayerPath(), player, fileHandler, serializer);

    System.out.println("[Level Manager]: Saved " + levelName);
  }

  public Entity loadGame(String levelName) {
    entityManager.loadEntities(getSavePath(levelName), fileHandler, serializer, true);
    Entity player = entityManager.loadEntity(getPlayerPath(), fileHandler, serializer, false);

    System.out.println("[Level Manager]: Loaded " + levelName);
    return player;
  }

  private String getSavePath(String levelName) {
    return "saves/levels/" + levelName + "/entities.json";
  }

  private String getPlayerPath() {
    return "saves/player.json";
  }
}
