# Hytale Template Plugin - Project Issues

This document outlines the issues discovered during a project analysis and provides a plan to resolve them.

**Last Updated:** February 1, 2026

---

## Status Summary

| Issue | Status | Description |
|-------|--------|-------------|
| Conflicting Build Files | ⚠️ Partial | Both files improved but conflict remains |
| Compile Error in build.gradle | ❌ Open | Still uses deprecated `OperatingSystem` API |
| Missing buildSrc Configuration | ❌ Open | Still missing `buildSrc/build.gradle.kts` |
| Missing Library Files | ❌ Open | `libs/` folder still empty |
| Property Name Mismatch | ⚠️ Partial | `hytale_build` added, others still mismatched |
| Duplicate Plugin Classes | ⚠️ Partial | Both still exist |
| Misleading Comments | ✅ Resolved | Fixed in `ExamplePlugin.java`, open in `TemplatePlugin.java` |

---

## Critical Issues

### 1. Conflicting Build Files

**Files Affected:** `build.gradle` (Groovy DSL) and `build.gradle.kts` (Kotlin DSL)

**Problem:** The project contains both Groovy and Kotlin DSL build files in the root directory. When both exist, Gradle prefers the Kotlin DSL (`build.gradle.kts`) over the Groovy DSL (`build.gradle`). These files have different but overlapping configurations:

| Aspect | `build.gradle` (Groovy) | `build.gradle.kts` (Kotlin) |
|--------|-------------------------|------------------------------|
| Plugins | `java`, `idea-ext`, `shadow` | `java-library`, `shadow`, `run-hytale` |
| Dependencies | From Hytale Maven + install path | From `./libs/HytaleServer.jar` |
| Hytale Detection | Automatic OS-based detection | Manual via libs folder |
| Shadow JAR | ✅ Included (v9.3.1) | ✅ Included (v9.3.1) |
| Run Config | IDEA run configuration + VSCode | Custom runServer task |
| Manifest Update | JSON manipulation task | Token replacement in processResources |

**Recent Improvements:**
- ✅ Shadow JAR plugin added to `build.gradle`
- ✅ Hytale Maven repositories added to `build.gradle`
- ✅ Dependencies now use Maven coordinates with `hytale_build` property
- ✅ VSCode launch configuration generation added

**Impact:** Build behavior is unpredictable; one configuration is ignored.

**Resolution Plan: Delete Kotlin DSL and Keep Groovy DSL**

The Groovy DSL (`build.gradle`) is now more feature-complete with:
- Automatic Hytale installation detection
- Both IntelliJ IDEA and VSCode run configurations
- Maven repository support for official Hytale server JAR
- Shadow JAR plugin for fat JAR creation

| Step | Action | Details |
|------|--------|---------|
| 1 | Delete `build.gradle.kts` | Remove Kotlin DSL to eliminate conflict |
| 2 | Fix OS detection in `build.gradle` | Replace broken `OperatingSystem` API (see Issue #2) |
| 3 | Delete `buildSrc/` folder | No longer needed without Kotlin DSL |
| 4 | Update documentation | Reflect the unified Groovy build system |

---

### 2. Compile Error in `build.gradle`

**File Affected:** `build.gradle` (Line 7)

**Problem:** The import `org.gradle.internal.os.OperatingSystem` uses an internal Gradle API that has been removed/moved in Gradle 9.x.

**Error:**
```
unable to resolve class org.gradle.internal.os.OperatingSystem
```

**Impact:** The Groovy build file cannot be used with Gradle 9.2.0.

**Resolution:** Replace with `System.getProperty("os.name")`:

```groovy
// Replace lines 14-26 with:
def osName = System.getProperty("os.name").toLowerCase()
if (osName.contains("windows")) {
    hytaleHome = "${System.getProperty("user.home")}/AppData/Roaming/Hytale"
}
else if (osName.contains("mac") || osName.contains("darwin")) {
    hytaleHome = "${System.getProperty("user.home")}/Library/Application Support/Hytale"
}
else if (osName.contains("linux")) {
    hytaleHome = "${System.getProperty("user.home")}/.var/app/com.hypixel.HytaleLauncher/data/Hytale"
    if (!file(hytaleHome).exists()) {
        hytaleHome = "${System.getProperty("user.home")}/.local/share/Hytale"
    }
}
```

---

### 3. Missing buildSrc Configuration

**File Missing:** `buildSrc/build.gradle.kts`

**Problem:** The `buildSrc/src/main/kotlin/RunHytalePlugin.kt` defines a custom Gradle plugin, but there is no build script for buildSrc itself.

**Impact:** The custom `run-hytale` plugin referenced in `build.gradle.kts` will not be available.

**Resolution:** If keeping Kotlin DSL, create `buildSrc/build.gradle.kts`:
```kotlin
plugins {
    `kotlin-dsl`
}

repositories {
    mavenCentral()
}
```

**Alternative:** If switching to Groovy DSL only, delete the entire `buildSrc/` folder as it's no longer needed.

---

### 4. Missing Required Files in `libs/`

**Files Missing:** `libs/HytaleServer.jar`, `libs/Assets.zip`

**Problem:** The `build.gradle.kts` references:
- `./libs/HytaleServer.jar` (for compilation and runtime)
- `./libs/Assets.zip` (for the runServer task)

The `libs/` folder currently only contains `.gitkeep` - no actual JAR or ZIP files.

**Impact:** If using Kotlin DSL, the project will fail to compile.

**Resolution:** This issue becomes irrelevant if switching to the Groovy DSL build, which:
- Downloads dependencies from Hytale's Maven repository
- Uses assets from the local Hytale installation

---

## Warnings

### 5. Property Name Mismatch

**Files Affected:** `build.gradle.kts`, `gradle.properties`

**Problem:** `build.gradle.kts` uses these property names:
- `pluginGroup` (defaults to "com.example")
- `pluginVersion` (defaults to "1.0.0")
- `pluginDescription` (defaults to "A Hytale plugin template")

But `gradle.properties` defines:
- `maven_group` (not `pluginGroup`)
- `version` (not `pluginVersion`)
- No description property

**Recent Additions to `gradle.properties`:**
- ✅ `hytale_build=2026.01.27-734d39026` - Used by the Groovy DSL for Maven dependencies

**Impact:** The Kotlin build file uses default values instead of the actual project properties.

**Resolution:** This becomes irrelevant if switching to Groovy DSL only, which uses:
- `version` ✅ (correct)
- `maven_group` ✅ (for publishing, if needed)
- `hytale_build` ✅ (for Maven dependency resolution)

---

### 6. Duplicate Plugin Implementations

**Files Affected:** 
- `src/main/java/com/example/templateplugin/TemplatePlugin.java`
- `src/main/java/org/example/plugin/ExamplePlugin.java`

**Problem:** The project contains two plugin entry points in different packages. The manifest (`manifest.json`) currently points to `org.example.plugin.ExamplePlugin`.

**Impact:** Potential confusion about which class is the actual entry point; unused code in the project.

**Recommendation:** Keep both as examples for now:
- `ExamplePlugin.java` - Minimal working example (used by manifest)
- `TemplatePlugin.java` - More comprehensive template with lifecycle methods

---

### 7. Misleading Comments in TemplatePlugin.java

**File Affected:** `src/main/java/com/example/templateplugin/TemplatePlugin.java`

**Problem:** The `registerEvents()` method has a Javadoc comment that says "Register your commands here" but the method is for registering events. The `registerCommands()` method also says "Register your commands here" which is correct but duplicated.

**Status:** 
- ✅ `ExamplePlugin.java` - Fixed (no longer has incorrect comments)
- ❌ `TemplatePlugin.java` - Still has incorrect comment in `registerEvents()` method

**Resolution:** Change comment in `registerEvents()`:
```java
/**
 * Register your events here.  // Changed from "commands"
 */
private void registerEvents() {
```

---

## Resolution Plan

### Recommended Approach: Keep Groovy DSL

The Groovy DSL (`build.gradle`) is now the more feature-complete option:

**Advantages:**
- ✅ Automatic Hytale installation detection (Windows, macOS, Linux)
- ✅ Official Hytale Maven repository integration
- ✅ IntelliJ IDEA run configuration generation
- ✅ VSCode launch.json generation
- ✅ Shadow JAR plugin for fat JAR creation
- ✅ Proper manifest.json updating

**Steps to Complete:**

| Step | Action | Priority |
|------|--------|----------|
| 1 | Fix `OperatingSystem` API in `build.gradle` | Critical |
| 2 | Delete `build.gradle.kts` | Critical |
| 3 | Delete `buildSrc/` folder | Critical |
| 4 | Fix comment in `TemplatePlugin.java` | Low |
| 5 | Update documentation | Medium |

---

## Quick Reference: File Status

| File | Status | Action Required |
|------|--------|-----------------|
| `build.gradle` | ⚠️ Error | Fix OperatingSystem API |
| `build.gradle.kts` | ⚠️ Redundant | Delete (conflicts with Groovy) |
| `gradle.properties` | ✅ OK | None |
| `settings.gradle` | ✅ OK | None |
| `gradle/wrapper/gradle-wrapper.properties` | ✅ OK | None |
| `buildSrc/` | ⚠️ Orphaned | Delete (not needed for Groovy) |
| `libs/` | ✅ OK | Not needed with Maven repos |
| `src/main/resources/manifest.json` | ✅ OK | None |
| `src/.../ExamplePlugin.java` | ✅ OK | None |
| `src/.../ExampleCommand.java` | ✅ OK | None |
| `src/.../TemplatePlugin.java` | ⚠️ Warning | Fix comment |

---

## Next Steps

1. Fix the `OperatingSystem` import in `build.gradle` (replace with `System.getProperty`)
2. Delete `build.gradle.kts` to resolve the conflict
3. Delete `buildSrc/` folder (no longer needed)
4. Run `./gradlew build` to verify the fixes
5. Optionally fix the comment in `TemplatePlugin.java`
6. Update documentation to reflect single build system
