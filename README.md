# luna-engine-java

A simple game engine library built in Java. This project is structured as a multi-module Gradle build, separating engine logic from game implementation.

## Modules
The engine is split into distinct libraries:

- **`math`**
  - Contains mathematical structures.
  - *Current Features:* `Vector2f` (2D vector operations).

- **`ecs`** (Entity Component System)
  - An Object-Oriented ECS.
  - Manages entities, component registration, and storage.
  - *Current Components:* `Transform` (Position, Velocity, Scale).

- **`dialogue`**
  - A dialogue system.
  - Uses **Directed Graphs** to allow for cyclic conversations.
  - Using JSON for loading conversations from external files.
  - Uses ID-based lookups, so entities can carry the necessary information to start a conversation.

- **`sandbox`**
  - The "Game" layer used to test and demonstrate engine features.

---

## How to run
- **`Linux / Mac`**
```bash
./gradlew run
```
- **`Windows`**
```bash
gradlew.bat run
```
