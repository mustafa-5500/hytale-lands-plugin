# manifest.json

**Location:** `src/main/resources/manifest.json`  
**Type:** JSON Configuration File  
**Purpose:** Plugin metadata and configuration - tells Hytale how to load your plugin

---

## Overview

The `manifest.json` file is the identity card of your plugin. Hytale reads this file to understand what your plugin is, who made it, and how to load it. Without a valid manifest, your plugin won't load.

---

## File Contents

```json
{
    "Group": "Example",
    "Name": "ExamplePlugin",
    "Version": "0.0.2",
    "Description": "An example plugin for HyTale!",
    "Authors": [
        {
            "Name": "It's you!"
        }
    ],
    "Website": "example.org",
    "ServerVersion": "*",
    "Dependencies": {
        
    },
    "OptionalDependencies": {
        
    },
    "DisabledByDefault": false,
    "Main": "org.example.plugin.ExamplePlugin",
    "IncludesAssetPack": true
}
```

---

## Field Reference

### Group

```json
"Group": "Example"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Publisher/organization identifier |
| **Format** | Usually your name or company |
| **Required** | Yes |

**Examples:**
- `"Hypixel"` - Official plugins
- `"YourName"` - Personal projects
- `"StudioName"` - Team projects

### Name

```json
"Name": "ExamplePlugin"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Plugin identifier (unique within Group) |
| **Format** | PascalCase, no spaces |
| **Required** | Yes |

**Full plugin ID:** `Group:Name` → `Example:ExamplePlugin`

### Version

```json
"Version": "0.0.2"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Plugin version number |
| **Format** | Semantic versioning (MAJOR.MINOR.PATCH) |
| **Required** | Yes |
| **Auto-Updated** | Yes, by build script |

**Note:** This is automatically synchronized with `gradle.properties` during build.

### Description

```json
"Description": "An example plugin for HyTale!"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Human-readable plugin description |
| **Format** | Plain text |
| **Required** | No (recommended) |

Shown in mod browsers and plugin lists.

### Authors

```json
"Authors": [
    {
        "Name": "It's you!"
    }
]
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | List of plugin creators |
| **Format** | Array of author objects |
| **Required** | No (recommended) |

**Extended author object:**
```json
"Authors": [
    {
        "Name": "Developer Name",
        "Contact": "email@example.com",
        "Website": "https://developer.com"
    },
    {
        "Name": "Contributor Name"
    }
]
```

### Website

```json
"Website": "example.org"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Plugin's homepage or documentation |
| **Format** | URL (with or without protocol) |
| **Required** | No |

### ServerVersion

```json
"ServerVersion": "*"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Compatible Hytale server versions |
| **Format** | Version constraint |
| **Required** | Yes |

**Values:**
- `"*"` - Any server version
- `"1.0.0"` - Exact version only
- `">=1.0.0"` - Version 1.0.0 or newer
- `">=1.0.0,<2.0.0"` - Range

Using `"*"` is common during development but should be specified for production.

### Dependencies

```json
"Dependencies": {
    
}
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Required plugins that must be loaded first |
| **Format** | Object with "Group:Name": "version" pairs |
| **Required** | No |

**Example with dependencies:**
```json
"Dependencies": {
    "Hypixel:CoreAPI": ">=1.0.0",
    "Example:Database": "2.0.0"
}
```

If dependencies aren't present, your plugin won't load.

### OptionalDependencies

```json
"OptionalDependencies": {
    
}
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Optional plugins for enhanced features |
| **Format** | Same as Dependencies |
| **Required** | No |

**Example:**
```json
"OptionalDependencies": {
    "Example:Economy": ">=1.0.0"
}
```

Your plugin loads even without these, but you should check at runtime:
```java
if (hasPlugin("Example:Economy")) {
    // Use economy features
}
```

### DisabledByDefault

```json
"DisabledByDefault": false
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Whether plugin starts disabled |
| **Values** | `true` or `false` |
| **Required** | No (defaults to false) |

Set to `true` for plugins that require manual configuration before use.

### Main

```json
"Main": "org.example.plugin.ExamplePlugin"
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Fully qualified name of main plugin class |
| **Format** | `package.ClassName` |
| **Required** | Yes |

**Critical:** This must exactly match your main class:
- Package: `org.example.plugin`
- Class: `ExamplePlugin`
- Full: `org.example.plugin.ExamplePlugin`

If this is wrong, the plugin won't load.

### IncludesAssetPack

```json
"IncludesAssetPack": true
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Whether plugin includes game assets |
| **Values** | `true` or `false` |
| **Required** | No (defaults to false) |
| **Auto-Updated** | Yes, from gradle.properties |

**When `true`:**
- Plugin can contain textures, models, sounds
- Assets in `resources/Server/` are loaded
- Custom items, blocks, recipes work
- In-game asset editor can modify plugin

**When `false`:**
- Code-only plugin
- Smaller JAR size
- Faster loading

---

## Directory Structure for Assets

When `IncludesAssetPack: true`, you can include game assets:

```
src/main/resources/
├── manifest.json
└── Server/
    ├── Item/
    │   ├── Definitions/
    │   │   └── Custom_Item.json
    │   └── Recipes/
    │       └── Custom_Recipe.json
    ├── Block/
    │   └── Definitions/
    │       └── Custom_Block.json
    └── ... (other asset types)
```

---

## Build Script Integration

The build scripts automatically update certain fields:

**gradle.properties:**
```properties
version=0.0.2
includes_pack=true
```

**During build:**
- `Version` is set to `0.0.2`
- `IncludesAssetPack` is set to `true`

This keeps manifest.json synchronized with build configuration.

---

## Validation Checklist

Before distributing your plugin, verify:

- [ ] `Group` is your identifier
- [ ] `Name` matches settings.gradle
- [ ] `Version` follows semantic versioning
- [ ] `Main` exactly matches your class
- [ ] `ServerVersion` is appropriate
- [ ] `IncludesAssetPack` matches your content
- [ ] No JSON syntax errors

---

## Common Errors

### "Failed to find main class"

```
Main class 'com.wrong.Package.Plugin' not found
```

**Fix:** Ensure `Main` exactly matches your class's package and name.

### "Invalid manifest"

**Fix:** Check JSON syntax - missing commas, brackets, quotes.

### "Plugin not appearing in mod list"

**Fix:** Check `IncludesAssetPack` and file locations.

---

## Related Files

- [ExamplePlugin.java.md](../java/org/example/plugin/ExamplePlugin.java.md) - Main class referenced by manifest
- [gradle.properties.md](../../../gradle.properties.md) - Version source
- [Example_Recipe.json.md](Server/Item/Recipes/Example_Recipe.json.md) - Example asset
