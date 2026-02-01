# Documentation Overview

This documentation folder mirrors the project structure and provides detailed explanations for each file in the Hytale Template Plugin project.

**Last Updated:** February 1, 2026

## What is This Project?

The **Hytale Template Plugin** is a production-ready starter template for creating server-side Java plugins for the game Hytale. It provides:

- A modern Gradle build system with Groovy DSL
- Automatic Hytale installation detection
- IDE integration (IntelliJ IDEA and VSCode)
- Two example plugin implementations (minimal and comprehensive)
- Example game content (crafting recipes)
- Official Hytale Maven repository integration

## Current Status

See [ISSUES.md](../ISSUES.md) for known issues and their resolution status.

| Component | Status |
|-----------|--------|
| Build System | ⚠️ Needs fix (deprecated API) |
| Plugin Code | ✅ Working |
| Documentation | ✅ Up to date |

## Documentation Structure

```
Documentation/
├── README.md                        # This file - Overview
├── build.gradle.md                  # Main Groovy DSL build script
├── build.gradle.kts.md              # Alternative Kotlin DSL build script
├── gradle.properties.md             # Project configuration properties
├── settings.gradle.md               # Gradle settings
├── gradlew.md                       # Gradle wrapper scripts
├── buildSrc/
│   └── src/main/kotlin/
│       └── RunHytalePlugin.kt.md    # Custom Gradle plugin (Kotlin DSL only)
├── gradle/
│   └── wrapper/
│       └── gradle-wrapper.properties.md  # Gradle wrapper configuration
└── src/
    └── main/
        ├── java/
        │   ├── com/example/templateplugin/
        │   │   └── TemplatePlugin.java.md    # Comprehensive plugin template
        │   └── org/example/plugin/
        │       ├── ExamplePlugin.java.md     # Main plugin (minimal example)
        │       └── ExampleCommand.java.md    # Command implementation example
        └── resources/
            ├── manifest.json.md              # Plugin metadata
            └── Server/Item/Recipes/
                └── Example_Recipe.json.md    # Example crafting recipe
```

## Quick Navigation

### Build System
- [build.gradle.md](build.gradle.md) - Main build configuration (Groovy DSL, recommended)
- [build.gradle.kts.md](build.gradle.kts.md) - Alternative Kotlin DSL build script
- [gradle.properties.md](gradle.properties.md) - Project properties (including `hytale_build`)
- [settings.gradle.md](settings.gradle.md) - Project settings

### Custom Tooling
- [RunHytalePlugin.kt.md](buildSrc/src/main/kotlin/RunHytalePlugin.kt.md) - Automated server testing plugin (Kotlin DSL only)

### Plugin Source Code
- [ExamplePlugin.java.md](src/main/java/org/example/plugin/ExamplePlugin.java.md) - Main plugin entry point (referenced in manifest)
- [TemplatePlugin.java.md](src/main/java/com/example/templateplugin/TemplatePlugin.java.md) - Comprehensive template with lifecycle methods
- [ExampleCommand.java.md](src/main/java/org/example/plugin/ExampleCommand.java.md) - Command implementation

### Resources
- [manifest.json.md](src/main/resources/manifest.json.md) - Plugin metadata
- [Example_Recipe.json.md](src/main/resources/Server/Item/Recipes/Example_Recipe.json.md) - Crafting recipe example

## Getting Started

1. Read [build.gradle.md](build.gradle.md) to understand the build system
2. Study [ExamplePlugin.java.md](src/main/java/org/example/plugin/ExamplePlugin.java.md) for the main plugin structure
3. Review [TemplatePlugin.java.md](src/main/java/com/example/templateplugin/TemplatePlugin.java.md) for a more comprehensive template
4. Check [manifest.json.md](src/main/resources/manifest.json.md) to understand plugin configuration

## Key Concepts

### Plugin Lifecycle
1. **Constructor** - Plugin is loaded, basic initialization
2. **setup()** - Register commands, events, services
3. **start()** - Plugin is enabled, start operations
4. **shutdown()** - Plugin is disabled, cleanup

### Build Commands
```bash
./gradlew build        # Build the plugin JAR (includes shadowJar)
./gradlew shadowJar    # Build only the shadow JAR
./gradlew clean build  # Clean rebuild
./gradlew generateVSCodeLaunch  # Generate VSCode debug configuration
```

### Key Properties (gradle.properties)
| Property | Purpose |
|----------|---------|
| `version` | Plugin version (semantic versioning) |
| `hytale_build` | Hytale build string for Maven dependency |
| `patchline` | Release channel (`release` or `pre-release`) |
| `includes_pack` | Whether plugin includes game assets |
