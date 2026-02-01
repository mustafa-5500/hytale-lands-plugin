# TemplatePlugin.java

**Location:** `src/main/java/com/example/templateplugin/TemplatePlugin.java`  
**Type:** Java Source File  
**Purpose:** Comprehensive plugin template with full lifecycle methods

---

## Overview

This is a more comprehensive plugin template that demonstrates all lifecycle methods (`setup()`, `start()`, `shutdown()`) and common plugin patterns like singleton access. Unlike `ExamplePlugin`, this template provides placeholder methods for you to fill in, making it ideal as a starting point for larger plugins.

**Note:** This class is NOT referenced in `manifest.json` - it's provided as an alternative template. The active plugin is `ExamplePlugin`.

---

## ⚠️ Known Issue

The `registerEvents()` method has an incorrect comment that says "Register your commands here" instead of "Register your events here". See [ISSUES.md](../../../ISSUES.md) for details.

---

## Full Source Code

```java
package com.example.templateplugin;

import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;

import javax.annotation.Nonnull;
import java.util.logging.Level;

public class TemplatePlugin extends JavaPlugin {

    private static TemplatePlugin instance;

    public TemplatePlugin(@Nonnull JavaPluginInit init) {
        super(init);
        instance = this;
        getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin loaded!");
    }

    public static TemplatePlugin getInstance() {
        return instance;
    }

    @Override
    protected void setup() {
        getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin setup!");
        registerEvents();
        registerCommands();
    }

    @Override
    protected void start() {
        getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin enabled!");
    }

    @Override
    public void shutdown() {
        getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin disabled!");
    }

    /**
     * Register your commands here.  // ⚠️ Should say "events"
     */
    private void registerEvents() {
        // TODO: Register event listeners
    }

    /**
     * Register your commands here.
     */
    private void registerCommands() {
        // TODO: Register commands
    }
}
```

---

## Class Breakdown

### Package Declaration

```java
package com.example.templateplugin;
```

**Customization:** Change to your own package:
```java
package com.yourname.yourplugin;
```

### Imports

```java
import com.hypixel.hytale.server.core.plugin.JavaPlugin;
import com.hypixel.hytale.server.core.plugin.JavaPluginInit;
import javax.annotation.Nonnull;
import java.util.logging.Level;
```

| Import | Purpose |
|--------|---------|
| `JavaPlugin` | Base class for all Hytale plugins |
| `JavaPluginInit` | Initialization data passed by server |
| `@Nonnull` | Annotation indicating parameter cannot be null |
| `Level` | Log level constants (INFO, WARNING, SEVERE) |

### Singleton Pattern

```java
private static TemplatePlugin instance;

public static TemplatePlugin getInstance() {
    return instance;
}
```

**Purpose:** Allows other classes to access the plugin instance:
```java
TemplatePlugin plugin = TemplatePlugin.getInstance();
plugin.getLogger().at(Level.INFO).log("Accessed from another class!");
```

**Why use this?**
- Access plugin methods from commands, listeners, etc.
- Get plugin configuration
- Access registered services
- Use the plugin's logger

---

## Plugin Lifecycle

### 1. Constructor

```java
public TemplatePlugin(@Nonnull JavaPluginInit init) {
    super(init);
    instance = this;
    getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin loaded!");
}
```

**When called:** When the server loads the plugin JAR  
**What to do here:**
- Call `super(init)` (required)
- Set singleton instance
- Minimal initialization only

**Don't do here:**
- Register commands or events
- Access other plugins
- Heavy operations

### 2. setup()

```java
@Override
protected void setup() {
    getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin setup!");
    registerEvents();
    registerCommands();
}
```

**When called:** After all plugins are loaded, before server starts  
**What to do here:**
- Load configuration files
- Register event listeners
- Register commands
- Initialize services
- Connect to databases

This is the main initialization phase.

### 3. start()

```java
@Override
protected void start() {
    getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin enabled!");
}
```

**When called:** When the server is fully started and ready  
**What to do here:**
- Start scheduled tasks
- Begin accepting player connections
- Enable features that need a running server

### 4. shutdown()

```java
@Override
public void shutdown() {
    getLogger().at(Level.INFO).log("[TemplatePlugin] Plugin disabled!");
}
```

**When called:** When the server is stopping  
**What to do here:**
- Save data to files/database
- Stop scheduled tasks
- Close connections
- Clean up resources

---

## Placeholder Methods

### registerEvents()

```java
private void registerEvents() {
    // TODO: Register event listeners
}
```

**Example implementation:**
```java
private void registerEvents() {
    this.getEventBus().register(new PlayerJoinListener());
    this.getEventBus().register(new BlockBreakListener());
}
```

### registerCommands()

```java
private void registerCommands() {
    // TODO: Register commands
}
```

**Example implementation:**
```java
private void registerCommands() {
    this.getCommandRegistry().registerCommand(new SpawnCommand());
    this.getCommandRegistry().registerCommand(new TeleportCommand());
}
```

---

## Logging

The template uses Hytale's structured logging:

```java
getLogger().at(Level.INFO).log("[TemplatePlugin] Message here!");
```

**Log Levels:**
| Level | Use Case |
|-------|----------|
| `Level.INFO` | Normal operations, status updates |
| `Level.WARNING` | Potential issues, recoverable errors |
| `Level.SEVERE` | Critical errors, failures |
| `Level.FINE` | Debug information |

**With variables:**
```java
getLogger().at(Level.INFO).log("Player %s joined the server!", playerName);
```

---

## Customization Guide

### Step 1: Rename Package

1. Change package declaration:
   ```java
   package com.yourname.yourplugin;
   ```

2. Move file to matching directory:
   ```
   src/main/java/com/yourname/yourplugin/YourPlugin.java
   ```

### Step 2: Rename Class

```java
public class YourPlugin extends JavaPlugin {
    private static YourPlugin instance;
    // ...
}
```

### Step 3: Update manifest.json

```json
{
    "Group": "YourName",
    "Name": "YourPlugin",
    "Main": "com.yourname.yourplugin.YourPlugin"
}
```

### Step 4: Add Features

```java
private void registerCommands() {
    this.getCommandRegistry().registerCommand(
        new MyCommand(this.getName())
    );
}

private void registerEvents() {
    this.getEventBus().register(new MyListener());
}
```

---

## Comparison with ExamplePlugin

| Aspect | TemplatePlugin | ExamplePlugin |
|--------|----------------|---------------|
| **Purpose** | Starting template | Working example |
| **Commands** | None (placeholder) | `/test` command |
| **Events** | None (placeholder) | None |
| **Logging** | Basic Java logging | HytaleLogger |
| **Complexity** | Minimal | Slightly more |
| **Use for** | New projects | Learning |

---

## Related Files

- [ExamplePlugin.java.md](../../../org/example/plugin/ExamplePlugin.java.md) - Working example
- [ExampleCommand.java.md](../../../org/example/plugin/ExampleCommand.java.md) - Command example
- [manifest.json.md](../../../../resources/manifest.json.md) - Plugin metadata
