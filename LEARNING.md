# Learning
For me personally to explain exactly what the files are doing when using gradle.

## The files
### 1. `settings.gradle` (Project Topology Definition)
This is the very first file Gradle reads, it builds the lifecycle's init phase.
* It defines the project hierarchy. It determines which directories are part of the build and assigns them logical project names.
* When Gradle starts, it looks for this file to build a project tree. If a folder exists on your disk but is not included here, Gradle ignores it completely.
* The `include` method creates instances for those directories, to be referenced by other projects later.

### 2. `build.gradle` (Build Configuration Script)
This file is the 'instruction manual', You have one in the root for shared rules and one in each folder for specific rules. Like when you apply `application` it injects the `run` task.
* It tells Gradle how to convert the files into a running program. It defines dependencies (libraries you need) and plugins.

### 3. The Wrapper (`gradlew` / `gradle/wrapper/`)
The Wrapper is a bootstrapping mechanism design to ensure Build Reproducibility. The great feature that ensures the project (on whatever host machine) always builds with the exact Gradle version specified.
* When original running the `gradle wrapper` it greats a `./gradlew` (Linux/Mac) and a `gradlew.bat` (Windows), which does just that, it parses the properties to find the Gradle version, checks users machine if it exists, if not, it downloads it and spawns a Java process to run it.
