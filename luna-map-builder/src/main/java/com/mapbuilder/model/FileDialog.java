package com.mapbuilder.model;

import org.lwjgl.system.MemoryStack;
import org.lwjgl.util.tinyfd.TinyFileDialogs;

import static org.lwjgl.system.MemoryStack.stackPush;

import org.lwjgl.PointerBuffer;

public final class FileDialog {
  public static String openFileDialog(
      String title,
      String defaultPathOrFile,
      String[] filterPatterns,
      String singleFilterDescription,
      boolean allowMultipleSelects) {
    if (filterPatterns == null || filterPatterns.length == 0) {
      return TinyFileDialogs.tinyfd_openFileDialog(title, defaultPathOrFile, null, singleFilterDescription,
          allowMultipleSelects);
    }

    /*
     * The TinyFileDialogs JNI needs raw off-heap memory address (pointers) and not
     * JVM heap. So the needed string array in C (char**) is done by just malloc
     * pointers.
     * The UTF8 helps with taking UTF-16 string and turning it into UTF-8 with null
     * terminator. And the flip (per description) switches the buffer from writing
     * mode to reading mode.
     */
    try (MemoryStack stack = stackPush()) {
      PointerBuffer filterBuffer = stack.mallocPointer(filterPatterns.length);
      for (String pattern : filterPatterns) {
        filterBuffer.put(stack.UTF8(pattern));
      }
      filterBuffer.flip();

      return TinyFileDialogs.tinyfd_openFileDialog(
          title,
          defaultPathOrFile,
          filterBuffer,
          singleFilterDescription,
          allowMultipleSelects);
    }
  }

  public static String saveFileDialog(
      String title,
      String defaultPathOrFile,
      String[] filterPatterns,
      String singleFilterDescription) {
    try (MemoryStack stack = stackPush()) {
      PointerBuffer filterBuffer = null;
      if (filterPatterns != null && filterPatterns.length > 0) {
        filterBuffer = stack.mallocPointer(filterPatterns.length);
        for (String pattern : filterPatterns) {
          filterBuffer.put(stack.UTF8(pattern));
        }
        filterBuffer.flip();
      }

      return TinyFileDialogs.tinyfd_saveFileDialog(
          title,
          defaultPathOrFile,
          filterBuffer,
          singleFilterDescription);
    }
  }
}
