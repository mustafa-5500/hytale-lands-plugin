# ExampleCommand.java

**Location:** `src/main/java/org/example/plugin/ExampleCommand.java`  
**Type:** Java Source File  
**Purpose:** Example command implementation demonstrating Hytale's command system

---

## Overview

This file demonstrates how to create a command that players can execute in-game. When a player types `/test`, they receive a greeting message from the plugin.

---

## Full Source Code

```java
package org.example.plugin;

import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;

import javax.annotation.Nonnull;

public class ExampleCommand extends CommandBase {

    private final String pluginName;
    private final String pluginVersion;

    public ExampleCommand(String pluginName, String pluginVersion) {
        super("test", "Prints a test message from the " + pluginName + " plugin.");
        this.setPermissionGroup(GameMode.Adventure);
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Hello from the " + pluginName + " v" + pluginVersion + " plugin!"));
    }
}
```

---

## Class Breakdown

### Imports

```java
import com.hypixel.hytale.protocol.GameMode;
import com.hypixel.hytale.server.core.Message;
import com.hypixel.hytale.server.core.command.system.CommandContext;
import com.hypixel.hytale.server.core.command.system.basecommands.CommandBase;
import javax.annotation.Nonnull;
```

| Import | Purpose |
|--------|---------|
| `GameMode` | Permission levels (Adventure, Creative, etc.) |
| `Message` | Chat message creation |
| `CommandContext` | Execution context (sender, arguments, etc.) |
| `CommandBase` | Base class for commands |
| `@Nonnull` | Null-safety annotation |

### Class Declaration

```java
public class ExampleCommand extends CommandBase {
```

All commands extend `CommandBase`, which provides:
- Command name and description
- Permission handling
- Argument parsing
- Tab completion
- Execution framework

### Instance Fields

```java
private final String pluginName;
private final String pluginVersion;
```

Stores plugin information passed through the constructor. Using `final` ensures these values can't change after construction.

---

## Constructor

```java
public ExampleCommand(String pluginName, String pluginVersion) {
    super("test", "Prints a test message from the " + pluginName + " plugin.");
    this.setPermissionGroup(GameMode.Adventure);
    this.pluginName = pluginName;
    this.pluginVersion = pluginVersion;
}
```

### super() Call

```java
super("test", "Prints a test message from the " + pluginName + " plugin.");
```

| Parameter | Value | Description |
|-----------|-------|-------------|
| Command name | `"test"` | Players type `/test` to execute |
| Description | Dynamic string | Shown in help, includes plugin name |

### Permission Setting

```java
this.setPermissionGroup(GameMode.Adventure);
```

**Permission Groups:**

| GameMode | Who Can Use |
|----------|-------------|
| `GameMode.Adventure` | All players (including non-operators) |
| `GameMode.Creative` | Players in creative mode or operators |
| (default) | Operators only |

By setting `GameMode.Adventure`, any player can use this command.

---

## Command Execution

```java
@Override
protected void executeSync(@Nonnull CommandContext ctx) {
    ctx.sendMessage(Message.raw("Hello from the " + pluginName + " v" + pluginVersion + " plugin!"));
}
```

### Method: executeSync

| Aspect | Description |
|--------|-------------|
| **When called** | When player executes the command |
| **Thread** | Server's main thread (synchronous) |
| **Parameter** | `CommandContext` with execution info |

**Why "Sync"?** There's also `executeAsync()` for commands that need to do I/O or other blocking operations without freezing the server.

### CommandContext

The `ctx` parameter provides:
- `ctx.sendMessage()` - Send message to command sender
- `ctx.getSender()` - Get the entity/player who ran command
- `ctx.getArguments()` - Get command arguments
- `ctx.getServer()` - Access server instance

### Message Creation

```java
Message.raw("Hello from the " + pluginName + " v" + pluginVersion + " plugin!")
```

`Message.raw()` creates a plain text message. Other options:
- `Message.raw(text)` - Plain text
- `Message.formatted(text)` - With formatting codes
- Custom Message builders for rich content

---

## Command Output

When a player types `/test`:

```
Hello from the ExamplePlugin v0.0.2 plugin!
```

---

## Creating Your Own Commands

### Basic Command Template

```java
public class MyCommand extends CommandBase {

    public MyCommand() {
        super("mycommand", "Description of what it does");
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("Command executed!"));
    }
}
```

### Command with Arguments

```java
public class GreetCommand extends CommandBase {

    public GreetCommand() {
        super("greet", "Greet a player");
        // Add argument specification here
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        String[] args = ctx.getArguments();
        if (args.length > 0) {
            ctx.sendMessage(Message.raw("Hello, " + args[0] + "!"));
        } else {
            ctx.sendMessage(Message.raw("Usage: /greet <name>"));
        }
    }
}
```

### Operator-Only Command

```java
public class AdminCommand extends CommandBase {

    public AdminCommand() {
        super("admin", "Admin-only command");
        // Don't set permission group - defaults to operator-only
    }

    @Override
    protected void executeSync(@Nonnull CommandContext ctx) {
        ctx.sendMessage(Message.raw("You have admin access!"));
    }
}
```

### Async Command (for I/O operations)

```java
public class DatabaseCommand extends CommandBase {

    public DatabaseCommand() {
        super("lookup", "Look up player in database");
    }

    @Override
    protected void executeAsync(@Nonnull CommandContext ctx) {
        // Safe to do blocking I/O here
        PlayerData data = database.lookup(ctx.getArguments()[0]);
        ctx.sendMessage(Message.raw("Found: " + data.toString()));
    }
}
```

---

## Registration

Commands must be registered in your plugin's `setup()` method:

```java
@Override
protected void setup() {
    this.getCommandRegistry().registerCommand(new ExampleCommand(
        this.getName(), 
        this.getManifest().getVersion().toString()
    ));
}
```

---

## Best Practices

### ✅ DO:

1. **Use descriptive command names** - `/teleport` not `/tp1`
2. **Provide helpful descriptions** - Shown in help system
3. **Validate arguments** - Check before using
4. **Handle errors gracefully** - Send user-friendly messages
5. **Set appropriate permissions** - Restrict dangerous commands

### ❌ DON'T:

1. **Block in executeSync** - Use executeAsync for I/O
2. **Throw unhandled exceptions** - Catch and report errors
3. **Hardcode messages** - Use configuration files
4. **Ignore edge cases** - Handle missing/invalid arguments

---

## Related Files

- [ExamplePlugin.java.md](ExamplePlugin.java.md) - Plugin that registers this command
- [TemplatePlugin.java.md](../../com/example/templateplugin/TemplatePlugin.java.md) - Alternative plugin structure
