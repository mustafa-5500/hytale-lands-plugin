# gradle.properties

**Location:** `gradle.properties` (Project Root)  
**Type:** Java Properties File  
**Purpose:** Centralized project configuration and build settings

---

## Overview

This file contains key-value properties that configure the Gradle build process and define project metadata. These values are accessible throughout all Gradle build scripts and can be overridden via command line.

---

## File Contents

```properties
# The current version of your project. Please use semantic versioning!
version=0.0.2

# The group ID used for maven publishing. Usually the same as your package name
# but not the same as your plugin group!
maven_group=org.example

# The version of Java used by your plugin. The game is built on Java 21 but
# actually runs on Java 25.
java_version=25

# Determines if your plugin should also be loaded as an asset pack. If your
# pack contains assets, or you intend to use the in-game asset editor, you
# want this to be true.
includes_pack=true

# The release channel your plugin should be built and ran against. This is
# usually release or pre-release. You can verify your settings in the
# official launcher.
patchline=release

# The exact Hytale build to compile against. Use the build string from the
# launcher (format YYYY.MM.DD-<hash>) so Gradle pulls the matching server jar
# for your selected patchline.
hytale_build=2026.01.27-734d39026

# Determines if the development server should also load mods from the user's
# standard mods folder. This lets you test mods by installing them where a
# normal player would, instead of adding them as dependencies or adding them
# to the development server manually.
load_user_mods=false

# If Hytale was installed to a custom location, you must set the home path
# manually. You may also want to use a custom path if you are building in
# a non-standard environment like a build server. The home path should
# the folder that contains the install and UserData folder.
# hytale_home=./test-file
```

---

## Property Reference

### version

```properties
version=0.0.2
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Plugin version number |
| **Format** | Semantic Versioning (MAJOR.MINOR.PATCH) |
| **Used In** | JAR filename, manifest.json, plugin identification |
| **Example** | `1.0.0`, `2.3.1`, `0.0.2` |

**Semantic Versioning:**
- **MAJOR** - Incompatible API changes
- **MINOR** - New features, backward compatible
- **PATCH** - Bug fixes, backward compatible

### maven_group

```properties
maven_group=org.example
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Maven group ID for artifact publishing |
| **Format** | Reverse domain notation |
| **Used In** | Maven publishing, artifact identification |
| **Example** | `com.yourcompany`, `io.github.username` |

**Note:** This is different from the plugin "Group" in manifest.json. The maven_group is for build/publishing, while the manifest Group is for in-game identification.

### java_version

```properties
java_version=25
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Target Java version for compilation |
| **Current Value** | 25 (Hytale's runtime) |
| **Used In** | Java toolchain configuration |

**Important:** While Hytale was built on Java 21, it runs on Java 25. Always use 25 for compatibility.

### includes_pack

```properties
includes_pack=true
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Whether plugin includes game assets |
| **Values** | `true` or `false` |
| **Used In** | manifest.json, server run arguments |

**When to use `true`:**
- Plugin contains custom textures, models, sounds
- Plugin adds items, blocks, recipes
- You want to use the in-game asset editor
- Plugin modifies any game content files

**When to use `false`:**
- Plugin is code-only (commands, events)
- No custom game assets included

### patchline

```properties
patchline=release
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Game release channel to build against |
| **Values** | `release`, `pre-release` |
| **Used In** | Finding HytaleServer.jar and Assets.zip |

**Channels:**
- **release** - Stable public builds
- **pre-release** - Beta/testing builds (may have newer features)

Verify your setting matches what's shown in the Hytale Launcher.

### hytale_build

```properties
hytale_build=2026.01.27-734d39026
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Exact Hytale build version for Maven dependency resolution |
| **Format** | `YYYY.MM.DD-<hash>` (from Hytale Launcher) |
| **Used In** | `build.gradle` Maven dependency declaration |

**Finding Your Build Version:**
1. Open the Hytale Launcher
2. Look for the build string in the format `YYYY.MM.DD-xxxxxxxx`
3. Copy and paste into this property

**Example Usage in build.gradle:**
```groovy
dependencies {
    compileOnly("com.hypixel.hytale:Server:$hytale_build")
}
```

### load_user_mods

```properties
load_user_mods=false
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Load mods from user's mods folder during development |
| **Values** | `true` or `false` |
| **Used In** | Development server configuration |

**When `true`:** The development server will also load mods from `UserData/Mods/`. Useful for testing compatibility with other mods.

**When `false`:** Only your plugin is loaded, providing a clean testing environment.

### hytale_home (Optional)

```properties
# hytale_home=./test-file
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | Custom Hytale installation path |
| **Default** | Auto-detected based on OS |
| **Used In** | Finding game files |

**Use cases:**
- Non-standard installation location
- CI/CD build servers
- Multiple Hytale installations
- Development without full game install

---

## Command Line Overrides

Any property can be overridden via command line:

```bash
# Override version
./gradlew build -Pversion=1.0.0

# Override multiple properties
./gradlew build -Pversion=1.0.0 -Pincludes_pack=false

# Set custom Hytale home
./gradlew build -Phytale_home=/path/to/hytale
```

---

## Environment-Specific Configurations

### Development

```properties
version=0.0.1-SNAPSHOT
includes_pack=true
load_user_mods=false
patchline=release
```

### Production Release

```properties
version=1.0.0
includes_pack=true
load_user_mods=false
patchline=release
```

### CI/CD Server

```properties
version=${CI_VERSION}
hytale_home=./libs
includes_pack=true
load_user_mods=false
```

---

## Related Files

- [build.gradle.kts.md](build.gradle.kts.md) - Uses these properties
- [build.gradle.md](build.gradle.md) - Uses these properties
- [manifest.json.md](src/main/resources/manifest.json.md) - Version synced from here
