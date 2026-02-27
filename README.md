# Luna Engine
Luna Engine is a custom 2D game engine framework and sandbox RPG built with Java. The project is structured as a multi-module Gradle workspace, separating pure game logic from the rendering.

![Game Showcase](docs/images/showcase.gif)

The workspace consists of:
- **luna-core**: The pure Java engine logic, containing the ECS, physics, dialogue graphs, and inventory systems.
- **luna-libgdx**: The LibGDX backend implementation handling rendering, assets, and window management on-top of **luna-core**.
- **sandbox**: The playable game application using the engine.

## Core Features
### Entity Component System (ECS)
The engine is using a ECS. Entities are managed through an `EntityManager` that maps components to entities. Entities and their components are entirely data-driven and can be loaded from and serialized to JSON files (save files).

### Dialogue System
A graph-based dialogue system. It supports dialogue nodes, player options, and logic conditions. The system features a typewriter text effect and uses the camera system to focus on the speaking entity.

### Inventory and Trade System
A inventory system handling item stacks, and currency. The `TradeManager` handles four-way trading (item-to-item, gold-to-item, item-to-gold, gold-to-gold) and enforces an "initiator disadvantage" logic, ensuring CPU entities only accept fair or profitable trades.

### Rendering and Camera
The rendering system supports Z-index sorting through `RenderLayerRegistry`. The `CameraSystem` mimics Unity's Cinemachine, following target entities with smooth lerping for both position and zoom, allowing for nice transitions during gameplay and dialogue.

### Physics and State Management
Includes physics implementation with `RigidBody`, `BoxCollider`, and `CircleCollider` components. The collision system supports trigger events (enter/leave) for environmental interactions (if one collider has trigger on). Game states (Loading, Menu, Game) are handled via a Finite State Machine (FSM).

### Level Loading and Future Map Builder
Levels, tilemaps, and entity states are saved and loaded via JSON. A dedicated Map Builder application is planned to help with level creation. It will allow importing tilesheets, painting levels, and exporting directly to the required JSON format.

## How to Run
### Windows:
```dos
gradlew.bat :sandbox:run
```

### Linux / macOS:
```bash
./gradlew :sandbox:run
```

## TODO
### Planned
- [ ] Add a UIComponent that contains a sprite, screen position, widht, height, and so on. So it will be rendered in the UIFrame on top of everything.
- [ ] Update README.md, been a while.
- [ ] Update and add tests, been a while. (AWARE)
- [ ] Implement a logger that neatly logs both terminal and file based on what you want.
- [ ] [DEPENDENT ABOVE] Use the logger, there exists many places that doesn't have great error handling.
- [ ] When implementing level switching, remember to update the GDXAssetManager dispose(), read comment above it in the code for more context.
- [ ] Create a separate app / library that let me create levels more easily. It should import tilesheets and let me paint or edit levels and it will
      translate that into my needed JSON format.
- [ ] Map editor: separate application that either creates or edits maps. Is able to paint tiles from tilemap and import and export the format
      that luna-engine uses for maps.
- [ ] Animation system
- [ ] Combat system
- [ ] CPU behaviour systems:
    - [ ] CPU Orchestrator
    - [X] Dialogue system
    - [ ] Moving system: Follow entity, follow path either cyclic (patroling) or linear (goes from A-...-Z then goes back Z-...-A)
    - [ ] Combat system
    - [ ] Stats: Same as player StatsComponent.java but will extend it to CPUStatsComponent and include some kind of attitude level that will determine how they talk
          and how trades work - Currently CPU trades only accept if they either go even or win, but if attitude is like 0-100 and 50 is neutral and is 40 (worsen) the CPU
          maybe only accept trades that makes them go >60% profit or something like that.

### Done
- [X] #ISSUE: The transparency when rendering is not working, even when entering alpha of 0.5f, it does nothing.
- [X] Implement a TradeManager in the inventory-system so that two inventories can perform a trade, this includes adding currency to a inventory that
      can be increased or decresed, then when two inventories want to trade they can trade gold (make sure both sides have enough), then one or the other
      maybe want to trade item for item (check worth, the initiator should always either make even or lose i.e., if you want to trade 100 currency sword
      for NPC 10 currency sword it should work, but not the other way around because NPC should never make a losing trade), then maybe they want to trade
      item for gold (check value of item <= gold amount).
- [X] Add a camera that acts like how you usually do it in Unity, have a entity with a camera attached and a system that uses it. It should follow an entity (player in begining),
      but be able to switch entity smoothly and zoom smoothly for other systems to use as they fit (dialogue)
- [X] [WIP] Add a level loader, so instead of loading in just entities per level, have a certain levels json that contains the whole tilemap aswell as the default stations for all entities
      so you load in that per level. So this includes adding a level loader that gets coordinates for sprites and what sprites aswell as coordinates for colliders (map corners).
      Then when you save and load like how it is now, it will still as it does now save a new state of the entiites from the level_one loaded entities but with newer data,
      so when you load a new level or load game it will try to load there first because that's the newest.
- [X] Implement the BoxCollider with the collision logic and then Implement a CircleCollider.
- [X] Make it easy to start dialogue with entities, like implement a proximity sensor between colliders like how Unity has Collider.OnTriggerEnter(Collider). Or loop
      through entities and check distance.
- [X] Extend collision system to implement how unity have a onTriggerEnter and onTriggerLeave that gets called once when entering and leaving other colliders
- [X] Add a RigidBody component that I can attach to entities so physics applies to them. It should also include data such as mass, (drag?), maybe now that
      I think of it, velocity should maybe be here instead of in the transform like I have it now. How much gravity, gravity toggle.
- [X] ~Create like an interface for the LIBGDX specific GameLayer implementaiton so I can have multiple levels (GameLayers) and also change so that each window/levels/GDXLevelLayer.java~
      ~which is general and implements~ GDXGameLayer SHOULD take in constructor level name so that I can load that specific entities json file, so for example:
        1. I give level name to LoadingLayer("level_one")
        2. It loads all the assets from assets/sprites/level_one
        3. It pushes a new level with name GameLayer("level_one") to fsm
        4. The GameLayers LoadGame() will load saves/level_one.json
      And then I have a way to intelligently load assets only from a level and entities from a specific level into a game. Probably need a separate .json file for the player that
      is always loaded I would guess because if not, when entering a new level, the player is recreated and doesn't save anything from previous level like items and so on.
- [X] Change the assets/sprites structure so I base it on level, So the loading layer should TAKE IN constructor parameter which is level name so that I can load assets
      from a specific levle instead of whole game, so the first loading layer like the one I have now only load in assets for that specific level so change
      it from this (assetManager.loadAllTextures("assets/sprites");) to (assetManager.loadAllTextures("assets/sprites/level_one");). So then in the actual game layer
      when I want to go to the next level I push a new LoadingLayer but with "assets/sprites/level_two" so that LoadingLayer can load in for level two and then push
      the next level with those assets and so on and so on.
- [X] Change so you load Level specific dialogue and not load every one every game layer (level).
- [X] Change the asset manager to load in multiple assets and both LOAD and REGISTER them in the registry automatically with easy spriteId so that I can
      set them easily in the entities.json when loading them in so I don't have to know the registry key before hand.
- [X] [DEPENDENT ABOVE] Add a loading screen to load all the assets, then push game layer.
- [X] [CONTROLLER NEW] Better would be to just create a pure data class cause I want serialization so it only contains numbers (easy to save inputs like that in JSON).
      So this logic module should not need to know about the movement system which should be implemented per framework (LibGDX, OpenGL, ...), so this
      component remains unchanged
- [X] Add serialization and deserialization so that I can save and load game, this can be used either just raw like I do now so you can save scum :)
      or the most probable function will be that each "actual" game implementaiton that is framework specific will have a LevelManager or something
      like that, that can use saveEntity and loadEntity to manage that per level saveGame("level_1/player_save.json") and saveGame("level_2/player_save.json")
      and so on so states can be remembered between levels.
- [X] Create a EntityManager that has mapping of Component classes to entitiies using them to avoid the issue where loading the game and updating the player all
      systems using player references gets stale-references, so now all accessing and adding is done through the list entities which is consistent.
      Also wanted to have mapping from class to list of entities so that for example the MovementSystem can get all entities with PlayerControllers components on them.
- [X] Add to EntityManager so that it can in its init (or constructor) load in and prepare entities with components from stored JSON file, so I can
      load in all entities I want from JSON instead of manually creating all in code which is both messy and annoying. So if I load in all entities and their
      components from JSON and then allow the game to easily access them with searching list and maps will be A GREAT ADDITION!!!
- [X] Create a StateManager which could possible used handling Game states such as MenuState, GameState, ... And could possibly be reused for other systems
- [X] Implement a render layer registry so I can register layer names (which is also stored in a SpriteRenderer component) that gets a certain value
      (what z-index to be rendered at). So that the render system sorts the entities based on value of the layer their SpriteRenderer has, so negative
      to positive in the list so that the sprites furthest back (negative) gets rendered first: (20, 10, -10, 100, -1000) -> (-1000, -10, 10, 20, 100)
