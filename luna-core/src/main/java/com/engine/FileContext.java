package com.engine;

// Had to create a file context just because I want the EntityManager to be
// the one that loads the entities from filesystem but it shouldn't care
// about the implementation just the abstracted results. This is just
// like the GameSystem interface but we force implementation of read and write
// this is a dependency inversion principle:
// (https://www.geeksforgeeks.org/system-design/dependecy-inversion-principle-solid/)
public interface FileContext {
  String readFile(String path);

  void writeFile(String path, String content);

  boolean exists(String path);
}
