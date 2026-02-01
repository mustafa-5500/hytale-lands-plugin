# build.gradle.kts

**Location:** `build.gradle.kts` (Project Root)  
**Type:** Gradle Build Script (Kotlin DSL)  
**Purpose:** Main build configuration for the Hytale plugin

---

## Overview

This is the primary build configuration file using Gradle's Kotlin DSL. It defines how the plugin is compiled, what dependencies it uses, how the JAR is packaged, and configures automated server testing.

---

## File Breakdown

### Plugins Block

```kotlin
plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.3.1"
    id("run-hytale")
}
```

| Plugin | Purpose |
|--------|---------|
| `java-library` | Standard Java library development support |
| `com.gradleup.shadow` | Creates "fat JAR" with bundled dependencies |
| `run-hytale` | Custom plugin for automated server testing (defined in `buildSrc/`) |

### Project Metadata

```kotlin
group = findProperty("pluginGroup") as String? ?: "com.example"
version = findProperty("pluginVersion") as String? ?: "1.0.0"
description = findProperty("pluginDescription") as String? ?: "A Hytale plugin template"
```

These values can be overridden in `gradle.properties` or passed via command line. They're used for:
- Maven artifact naming
- JAR file naming
- Manifest generation

### Repositories

```kotlin
repositories {
    mavenLocal()
    mavenCentral()
}
```

- **mavenLocal()** - Local Maven repository (~/.m2/repository)
- **mavenCentral()** - Maven Central for public dependencies

### Dependencies

```kotlin
dependencies {
    // Hytale Server API (provided by server at runtime)
    compileOnly(files("./libs/HytaleServer.jar"))
    
    // Common dependencies (will be bundled in JAR)
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("org.jetbrains:annotations:24.1.0")
    
    // Test dependencies
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}
```

| Scope | Dependency | Purpose |
|-------|------------|---------|
| `compileOnly` | HytaleServer.jar | Hytale API - provided at runtime by server |
| `implementation` | Gson 2.10.1 | JSON parsing library (bundled) |
| `implementation` | JetBrains Annotations | `@Nullable`, `@NotNull` annotations |
| `testImplementation` | JUnit Jupiter 5.10.0 | Unit testing framework |

**Key Concept:** `compileOnly` means the dependency is needed for compilation but NOT bundled in the final JAR (the server provides it). `implementation` dependencies ARE bundled.

### Server Testing Configuration

```kotlin
runHytale {
    jarUrl = "./libs/HytaleServer.jar"
    assetsPath = "./libs/Assets.zip"
}
```

Configures the custom `run-hytale` plugin:
- **jarUrl** - Location of the Hytale server JAR
- **assetsPath** - Location of game assets (required for server)

### Task Configurations

#### Java Compilation

```kotlin
compileJava {
    options.encoding = Charsets.UTF_8.name()
    options.release = 25
}
```

- Uses UTF-8 encoding for source files
- Targets Java 25 (Hytale's runtime version)

#### Resource Processing

```kotlin
processResources {
    filteringCharset = Charsets.UTF_8.name()
    
    val props = mapOf(
        "group" to project.group,
        "version" to project.version,
        "description" to project.description
    )
    inputs.properties(props)
    
    filesMatching("manifest.json") {
        expand(props)
    }
}
```

**Key Feature:** Automatically replaces placeholders in `manifest.json` with actual project values during build. This keeps version numbers synchronized.

#### ShadowJar Configuration

```kotlin
shadowJar {
    archiveBaseName.set(rootProject.name)
    archiveClassifier.set("")
    
    // Relocate dependencies to avoid conflicts
    relocate("com.google.gson", "com.yourplugin.libs.gson")
    
    // Minimize JAR size (removes unused classes)
    minimize()
}
```

| Setting | Purpose |
|---------|---------|
| `archiveBaseName` | Output JAR name (uses project name) |
| `archiveClassifier` | Removes "-all" suffix from JAR name |
| `relocate()` | Renames packages to avoid classpath conflicts |
| `minimize()` | Removes unused classes to reduce JAR size |

**Why Relocation?** If multiple plugins use Gson, they might use different versions. Relocation renames your Gson to a unique package so it won't conflict with other plugins.

#### Test Configuration

```kotlin
test {
    useJUnitPlatform()
}
```

Configures tests to use JUnit 5 (Jupiter) platform.

#### Build Task

```kotlin
build {
    dependsOn(shadowJar)
}
```

Makes `./gradlew build` automatically create the shadow JAR.

### Java Toolchain

```kotlin
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
```

Ensures Gradle uses Java 25 for compilation, regardless of the JDK running Gradle itself.

---

## Common Commands

| Command | Description |
|---------|-------------|
| `./gradlew shadowJar` | Build plugin JAR with dependencies |
| `./gradlew runServer` | Build and start test server |
| `./gradlew test` | Run unit tests |
| `./gradlew clean build` | Clean rebuild |
| `./gradlew dependencies` | List all dependencies |

---

## Output

After building, the plugin JAR is located at:
```
build/libs/ExamplePlugin-0.0.2.jar
```

---

## Customization Guide

### Adding a New Dependency

```kotlin
dependencies {
    // For dependencies that should be bundled:
    implementation("group:artifact:version")
    
    // For compile-time only (provided by server):
    compileOnly("group:artifact:version")
}
```

### Changing Java Version

Update both locations:
```kotlin
compileJava {
    options.release = 25  // Change this
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))  // And this
    }
}
```

### Adding Another Relocated Dependency

```kotlin
shadowJar {
    relocate("original.package", "com.yourplugin.libs.relocated")
}
```

---

## Related Files

- [gradle.properties.md](gradle.properties.md) - Project properties
- [settings.gradle.md](settings.gradle.md) - Project settings
- [RunHytalePlugin.kt.md](buildSrc/src/main/kotlin/RunHytalePlugin.kt.md) - Server testing plugin
