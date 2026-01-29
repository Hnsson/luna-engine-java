# TODO
- [ ] Add a UIComponent that contains a sprite, screen position, widht, height, and so on. So it will be rendered in the UIFrame on top of everything.
- [ ] Update README.md, been a while.
- [ ] Implement a logger that neatly logs both terminal and file based on what you want.
- [ ] [DEPENDENT ABOVE] Use the logger, there exists many places that doesn't have great error handling.
- [ ] Make it easy to start dialogue with entities, like implement a proximity sensor between colliders like how Unity has Collider.OnTriggerEnter(Collider). Or loop
      through entities and check distance.
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
- [ ] When implementing level switching, remember to update the GDXAssetManager dispose(), read comment above it for more context.
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
      such as handling player states such as DuckingState, IdleState, AttackState. So the main game has a StateManager object handling the Game screens
      while the movement system has another StateManager object handling the player states.
- [ ] Implement the BoxCollider with the collision logic and then Implement a CircleCollider.
- [ ] Probably need a camera interface because the cameras are different in each framework? If I don't just wanna have it like now where the framework
      Implement handles that at their own accord.
- [ ] Implement a render layer registry so I can register layer names (which is also stored in a SpriteRenderer component) that gets a certain value
      (what z-index to be rendered at). So that the render system sorts the entities based on value of the layer their SpriteRenderer has, so negative
      to positive in the list so that the sprites furthest back (negative) gets rendered first: (20, 10, -10, 100, -1000) -> (-1000, -10, 10, 20, 100)
- [ ] Implement a TradeManager in the inventory-system so that two inventories can perform a trade, this includes adding currency to a inventory that
      can be increased or decresed, then when two inventories want to trade they can trade gold (make sure both sides have enough), then one or the other
      maybe want to trade item for item (check worth, the initiator should always either make even or lose i.e., if you want to trade 100 currency sword
      for NPC 10 currency sword it should work, but not the other way around because NPC should never make a losing trade), then maybe they want to trade
      item for gold (check value of item <= gold amount).
- [ ] Add a RigidBody component that I can attach to entities so physics applies to them. It should also include data such as mass, (drag?), maybe now that
      I think of it, velocity should maybe be here instead of in the transform like I have it now. How much gravity, gravity toggle.
