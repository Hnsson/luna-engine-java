# TODO
- [ ] Add a UIComponent that contains a sprite, screen position, widht, height, and so on. So it will be rendered in the UIFrame on top of everything.
- [ ] Add a ControllerContext component that can be defined by each framework and attached to player to make it move. Has attributes that
      are universal such as raw value (-1 to 1), clamped magnitude, maybe even deadzone adjusted value (if 0.1 return 0 => stick drift)
      direction vector (normalized), doubletap, more.
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
