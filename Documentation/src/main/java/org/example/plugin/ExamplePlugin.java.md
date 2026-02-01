# ExamplePlugin.java

**Location:** `src/main/java/org/example/plugin/ExamplePlugin.java`  
**Type:** Java Source File  
**Purpose:** Main plugin entry point - minimal working example with command registration

---

## Overview

This is the main plugin class referenced in `manifest.json`. It demonstrates proper plugin structure with a clean, minimal implementation that registers a working `/test` command. This is the recommended starting point for understanding Hytale plugin development.

---

## Full Source Code

```java
package org.example.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;

public class ExamplePlugin extends JavaPlugin {

    private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();

    public ExamplePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
    }

    @Override
    protected void setup() {
        LOGGER.atInfo().log("Setting up plugin " + this.getName());
        this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    }
}
```

---

## Class Breakdown

### Package and Imports

```java
package org.example.plugin;

import com.hypixel.hytale.logger.HytaleLogger;
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import javax.annotation.Nonnull;
```

| Import | Purpose |
|--------|---------|
| `HytaleLogger` | Hytale's structured logging system (Google Flogger) |
| `JavaPlugin` | Base class for all Hytale plugins |
| `JavaPluginInit` | Server-provided initialization data |
| `@Nonnull` | Null-safety annotation |

### Logger Setup

```java
private static final HytaleLogger LOGGER = HytaleLogger.forEnclosingClass();
```

**Key Points:**
- `static final` - One logger instance shared by all plugin instances
- `forEnclosingClass()` - Automatically uses class name for log context
- Better than `getLogger()` for static contexts

**Usage:**
```java
LOGGER.atInfo().log("Information message");
LOGGER.atWarning().log("Warning message");
LOGGER.atSevere().log("Error message");
```

---

## Plugin Lifecycle

### Constructor

```java
public ExamplePlugin(@Nonnull JavaPluginInit init) {
    super(init);
    LOGGER.atInfo().log("Hello from " + this.getName() + " version " + this.getManifest().getVersion().toString());
}
```

**What happens:**
1. Calls parent constructor with init data
2. Logs plugin name and version from manifest

**Accessing Manifest Data:**
- `this.getName()` - Returns plugin name
- `this.getManifest()` - Full manifest object
- `this.getManifest().getVersion()` - Version object

### setup() Method

```java
@Override
protected void setup() {
    LOGGER.atInfo().log("Setting up plugin " + this.getName());
    this.getCommandRegistry().registerCommand(
        new ExampleCommand(this.getName(), this.getManifest().getVersion().toString())
    );
}
```

**What happens:**
1. Logs setup message
2. Gets the command registry from the plugin
3. Creates and registers `ExampleCommand`
4. Passes plugin name and version to command

---

## Key APIs Demonstrated

### Command Registry

```java
this.getCommandRegistry().registerCommand(command);
```

The command registry is obtained from the plugin instance and used to register commands that players can execute.

**Pattern:**
```java
// Create command instance
ExampleCommand cmd = new ExampleCommand(name, version);

// Register with server
this.getCommandRegistry().registerCommand(cmd);
```

### Manifest Access

```java
// Plugin name (from manifest)
String name = this.getName();

// Full manifest object
PluginManifest manifest = this.getManifest();

// Version as Version object
Version version = manifest.getVersion();

// Version as string
String versionStr = version.toString();
```

The manifest contains all metadata from `manifest.json`:
- Group
- Name
- Version
- Description
- Authors
- Dependencies
- Main class
- etc.

### HytaleLogger vs getLogger()

| Method | Type | Best For |
|--------|------|----------|
| `LOGGER.atInfo()` | HytaleLogger (static) | Static methods, consistent logging |
| `getLogger().at(Level.INFO)` | Instance logger | Instance methods, plugin-specific |

**HytaleLogger advantages:**
- Fluent API (`atInfo()` vs `at(Level.INFO)`)
- Automatic class name context
- Works in static methods
- More concise

---

## Design Patterns

### Dependency Injection (Command)

```java
new ExampleCommand(this.getName(), this.getManifest().getVersion().toString())
```

The command receives its dependencies (name, version) through the constructor rather than accessing them statically. This makes the command:
- Easier to test
- More reusable
- Loosely coupled from plugin

### Minimal Code Philosophy

This example includes only what's necessary:
- No singleton pattern (not needed here)
- No `start()` override (nothing to do on start)
- No `shutdown()` override (nothing to clean up)
- No event listeners (keeping it simple)

---

## Comparison with TemplatePlugin

| Aspect | ExamplePlugin | TemplatePlugin |
|--------|---------------|----------------|
| **Logger** | HytaleLogger (modern) | Java Logger (basic) |
| **Singleton** | No | Yes |
| **Commands** | `/test` registered | Placeholder only |
| **Lifecycle** | Constructor + setup | All four methods |
| **Purpose** | Working example | Clean starting point |

---

## Extending This Example

### Adding More Commands

```java
@Override
protected void setup() {
    LOGGER.atInfo().log("Setting up plugin " + this.getName());
    
    // Register multiple commands
    this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    this.getCommandRegistry().registerCommand(new SpawnCommand());
    this.getCommandRegistry().registerCommand(new HelpCommand(this));
}
```

### Adding Event Listeners

```java
@Override
protected void setup() {
    LOGGER.atInfo().log("Setting up plugin " + this.getName());
    
    // Register commands
    this.getCommandRegistry().registerCommand(new ExampleCommand(this.getName(), this.getManifest().getVersion().toString()));
    
    // Register event listeners
    this.getEventBus().register(new PlayerJoinListener());
    this.getEventBus().register(new ChatListener());
}
```

### Adding Shutdown Logic

```java
@Override
public void shutdown() {
    LOGGER.atInfo().log("Shutting down " + this.getName());
    
    // Save data
    savePlayerData();
    
    // Close connections
    database.close();
}
```

---

## Related Files

- [ExampleCommand.java.md](ExampleCommand.java.md) - The command this plugin registers
- [TemplatePlugin.java.md](../../com/example/templateplugin/TemplatePlugin.java.md) - Alternative minimal template
- [manifest.json.md](../../../resources/manifest.json.md) - Plugin metadata
