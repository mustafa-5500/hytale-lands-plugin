# build.gradle

**Location:** `build.gradle` (Project Root)  
**Type:** Gradle Build Script (Groovy DSL)  
**Purpose:** Primary build configuration with IDE integration and automatic Hytale detection

---

## Overview

This is the main build script using Gradle's Groovy DSL. It provides automatic Hytale installation detection, IntelliJ IDEA and VSCode run configuration generation, Shadow JAR creation, and integration with Hytale's official Maven repositories.

---

## File Breakdown

### Plugins Block

```groovy
plugins {
    id 'java'
    id 'org.jetbrains.gradle.plugin.idea-ext' version '1.3'
    id 'com.gradleup.shadow' version '9.3.1'
}
```

| Plugin | Purpose |
|--------|---------|
| `java` | Standard Java development support |
| `idea-ext` | IntelliJ IDEA integration and run configuration generation |
| `shadow` | Creates "fat JAR" with bundled dependencies |

### Hytale Installation Detection

```groovy
ext {
    if (project.hasProperty('hytale_home')) {
        hytaleHome = project.findProperty('hytale_home')
    }
    else {
        def os = OperatingSystem.current()
        if (os.isWindows()) {
            hytaleHome = "${System.getProperty("user.home")}/AppData/Roaming/Hytale"
        }
        else if (os.isMacOsX()) {
            hytaleHome = "${System.getProperty("user.home")}/Library/Application Support/Hytale"
        }
        else if (os.isLinux()) {
            hytaleHome = "${System.getProperty("user.home")}/.var/app/com.hypixel.HytaleLauncher/data/Hytale"
            if (!file(hytaleHome).exists()) {
                hytaleHome = "${System.getProperty("user.home")}/.local/share/Hytale"
            }
        }
    }
}
```

**Automatic Detection Paths:**

| OS | Default Path |
|----|--------------|
| Windows | `%APPDATA%/Hytale` |
| macOS | `~/Library/Application Support/Hytale` |
| Linux (Flatpak) | `~/.var/app/com.hypixel.HytaleLauncher/data/Hytale` |
| Linux (Native) | `~/.local/share/Hytale` |

**Custom Path:** Set `hytale_home` property in `gradle.properties` or via command line:
```bash
./gradlew build -Phytale_home=/custom/path/to/hytale
```

### Validation

```groovy
if (!project.hasProperty('hytaleHome')) {
    throw new GradleException('Your Hytale install could not be detected automatically...')
}
else if (!file(project.findProperty('hytaleHome')).exists()) {
    throw new GradleException("Failed to find Hytale at the expected location...")
}
```

Ensures Hytale is installed before attempting to build. This is required because the build depends on the game's server JAR.

### Java Configuration

```groovy
java {
    toolchain.languageVersion = JavaLanguageVersion.of(java_version)
    withSourcesJar()
    withJavadocJar()
}

javadoc {
    options.addStringOption('Xdoclint:-missing', '-quiet')
}
```

| Feature | Purpose |
|---------|---------|
| `toolchain` | Uses Java version from `gradle.properties` |
| `withSourcesJar()` | Generates source JAR for distribution |
| `withJavadocJar()` | Generates Javadoc JAR for distribution |
| `Xdoclint:-missing` | Suppresses warnings about missing Javadocs |

### Dependencies

```groovy
dependencies {
    compileOnly("com.hypixel.hytale:Server:$hytale_build")
    if (hasHytaleHome) {
        runtimeOnly(files("$hytaleHome/install/$patchline/package/game/latest/Server/HytaleServer.jar"))
    }
}
```

Dependencies are resolved from Hytale's official Maven repository using the `hytale_build` property from `gradle.properties`. The local installation JAR is added for runtime if Hytale is installed locally.

**Key Properties:**
- `$hytale_build` - Build version from `gradle.properties` (e.g., `2026.01.27-734d39026`)
- `$patchline` - Release channel (`release` or `pre-release`)

### Repositories

```groovy
repositories {
    mavenCentral()
    maven {
        name = "hytale-release"
        url = uri("https://maven.hytale.com/release")
    }
    maven {
        name = "hytale-pre-release"
        url = uri("https://maven.hytale.com/pre-release")
    }
}
```

| Repository | Purpose |
|------------|---------|
| `mavenCentral()` | Standard Maven dependencies |
| `hytale-release` | Official Hytale release artifacts |
| `hytale-pre-release` | Official Hytale pre-release/beta artifacts |

### Server Run Directory

```groovy
def serverRunDir = file("$projectDir/run")
if (!serverRunDir.exists()) {
    serverRunDir.mkdirs()
}
```

Creates the `run/` directory for server files if it doesn't exist.

### Manifest Update Task

```groovy
tasks.register('updatePluginManifest') {
    def manifestFile = file('src/main/resources/manifest.json')
    doLast {
        if (!manifestFile.exists()) {
            throw new GradleException("Could not find manifest.json at ${manifestFile.path}!")
        }
        def manifestJson = new groovy.json.JsonSlurper().parseText(manifestFile.text)
        manifestJson.Version = version
        manifestJson.IncludesAssetPack = includes_pack.toBoolean()
        manifestFile.text = groovy.json.JsonOutput.prettyPrint(groovy.json.JsonOutput.toJson(manifestJson))
    }
}

tasks.named('processResources') {
    dependsOn 'updatePluginManifest'
}
```

**Purpose:** Automatically updates `manifest.json` with:
- Current version from `gradle.properties`
- Whether the plugin includes an asset pack

This ensures the manifest stays synchronized with build properties.

### Server Run Arguments Helper

```groovy
def createServerRunArguments(String srcDir) {
    def programParameters = '--allow-op --disable-sentry --assets="' + "${hytaleHome}/install/$patchline/package/game/latest/Assets.zip" + '"'
    def modPaths = []
    if (includes_pack.toBoolean()) {
        modPaths << srcDir
    }
    if (load_user_mods.toBoolean()) {
        modPaths << "${hytaleHome}/UserData/Mods"
    }
    if (!modPaths.isEmpty()) {
        programParameters += ' --mods="' + modPaths.join(',') + '"'
    }
    return programParameters
}
```

Generates command-line arguments for running the Hytale server with:
- `--allow-op` - Enables operator permissions
- `--disable-sentry` - Disables error reporting
- `--assets` - Path to game assets
- `--mods` - Paths to load mods from

### IntelliJ IDEA Run Configuration

```groovy
idea.project.settings.runConfigurations {
    'HytaleServer'(org.jetbrains.gradle.ext.Application) {
        mainClass = 'com.hypixel.hytale.Main'
        // ... additional configuration
    }
}
```

Automatically generates an IntelliJ IDEA run configuration named "HytaleServer" that:
- Runs the Hytale server main class
- Includes proper classpath and arguments
- Enables one-click debugging from the IDE

### VSCode Launch Configuration

```groovy
tasks.register('generateVSCodeLaunch') {
    def vscodeDir = file("$projectDir/.vscode")
    def launchFile = file("$vscodeDir/launch.json")
    // ... generates launch.json
}
```

Generates a `.vscode/launch.json` file for debugging in VSCode with:
- Java debug configuration
- Proper working directory
- Server arguments including assets and mods paths

Run `./gradlew generateVSCodeLaunch` to create the configuration.

### Shadow JAR Configuration

```groovy
tasks.named('shadowJar') {
    archiveClassifier.set('')
    mergeServiceFiles()
}

tasks.named('build') {
    dependsOn 'shadowJar'
}
```

| Setting | Purpose |
|---------|---------|
| `archiveClassifier.set('')` | Removes "-all" suffix from JAR name |
| `mergeServiceFiles()` | Properly merges META-INF/services files |

The shadow JAR is automatically built when running `./gradlew build`.

---

## Common Commands

| Command | Description |
|---------|-------------|
| `./gradlew build` | Build plugin JAR with dependencies |
| `./gradlew shadowJar` | Build only the shadow JAR |
| `./gradlew generateVSCodeLaunch` | Generate VSCode launch configuration |
| `./gradlew clean build` | Clean rebuild |
| `./gradlew dependencies` | List all dependencies |

---

## Output

After building, the plugin JAR is located at:
```
build/libs/ExamplePlugin-0.0.2.jar
```

---

## Comparison: build.gradle vs build.gradle.kts

| Feature | build.gradle (Groovy) | build.gradle.kts (Kotlin) |
|---------|----------------------|---------------------------|
| Syntax | Dynamic, flexible | Type-safe, IDE support |
| IDE Integration | IDEA + VSCode run configs | Custom run-hytale plugin |
| Hytale Detection | Automatic from install | Manual path in libs/ |
| Shadow JAR | ✅ Included | ✅ Included |
| Maven Repos | ✅ Official Hytale repos | ❌ Local files only |
| Recommended | ✅ For most users | For CI/CD portability |

---

## Known Issues

⚠️ **Gradle 9.x Compatibility:** The `org.gradle.internal.os.OperatingSystem` import on line 7 uses a deprecated internal API. See [ISSUES.md](../ISSUES.md) for the fix.

---

## Related Files

- [build.gradle.kts.md](build.gradle.kts.md) - Kotlin DSL build script (alternative)
- [gradle.properties.md](gradle.properties.md) - Project properties
- [settings.gradle.md](settings.gradle.md) - Project settings
