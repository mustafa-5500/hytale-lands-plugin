# settings.gradle

**Location:** `settings.gradle` (Project Root)  
**Type:** Gradle Settings Script (Groovy)  
**Purpose:** Defines project name and structure

---

## Overview

The `settings.gradle` file is executed before any build script and is used to configure the project structure. In this template, it simply defines the root project name.

---

## File Contents

```groovy
rootProject.name = 'ExamplePlugin'
```

---

## Property Reference

### rootProject.name

| Aspect | Description |
|--------|-------------|
| **Purpose** | Defines the project/plugin name |
| **Used For** | JAR filename, project identification |
| **Current Value** | `ExamplePlugin` |

This name appears in:
- Output JAR filename: `ExamplePlugin-0.0.2.jar`
- Gradle project references
- IDE project name

---

## Customization

When creating your own plugin, change this to your plugin name:

```groovy
rootProject.name = 'MyAwesomePlugin'
```

**Naming Conventions:**
- Use PascalCase (e.g., `MyPlugin`, `CoolFeatures`)
- Avoid spaces and special characters
- Keep it concise but descriptive
- Match the `Name` field in `manifest.json`

---

## Multi-Project Builds

For larger projects with submodules, `settings.gradle` can define multiple projects:

```groovy
rootProject.name = 'MyPluginSuite'

include 'core'
include 'api'
include 'modules:combat'
include 'modules:economy'
```

This template uses a single-project structure for simplicity.

---

## Kotlin DSL Alternative

If using `settings.gradle.kts` (Kotlin DSL):

```kotlin
rootProject.name = "ExamplePlugin"
```

---

## Initialization Order

Gradle executes files in this order:
1. `settings.gradle` - Project structure
2. `buildSrc/` - Custom plugins compiled
3. `build.gradle` / `build.gradle.kts` - Build configuration

---

## Related Files

- [build.gradle.kts.md](build.gradle.kts.md) - Main build configuration
- [build.gradle.md](build.gradle.md) - Alternative build configuration
- [manifest.json.md](src/main/resources/manifest.json.md) - Should have matching Name
