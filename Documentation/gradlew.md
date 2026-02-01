# gradlew / gradlew.bat

**Location:** `gradlew` (Unix) and `gradlew.bat` (Windows) in Project Root  
**Type:** Shell/Batch Scripts  
**Purpose:** Gradle Wrapper - Ensures consistent Gradle version across all environments

---

## Overview

The Gradle Wrapper is a script that automatically downloads and uses a specific version of Gradle, ensuring all developers and CI/CD systems use the same version regardless of what's installed on their system.

---

## Files

| File | Platform | Description |
|------|----------|-------------|
| `gradlew` | Unix/Linux/macOS | Bash shell script |
| `gradlew.bat` | Windows | Batch script |

---

## Usage

### Windows

```batch
gradlew.bat <task>

REM Examples:
gradlew.bat shadowJar
gradlew.bat runServer
gradlew.bat clean build
gradlew.bat test
```

### Unix/Linux/macOS

```bash
./gradlew <task>

# Examples:
./gradlew shadowJar
./gradlew runServer
./gradlew clean build
./gradlew test
```

**Note:** On Unix systems, you may need to make the script executable first:
```bash
chmod +x gradlew
```

---

## How It Works

1. **Script Execution:** When you run `./gradlew`, the script checks if the required Gradle version is cached
2. **Download (if needed):** If not cached, it downloads from `distributionUrl` in `gradle-wrapper.properties`
3. **Cache Location:** Downloaded to `~/.gradle/wrapper/dists/`
4. **Execution:** Runs the cached Gradle with your specified tasks

---

## Common Tasks

| Task | Description |
|------|-------------|
| `shadowJar` | Build plugin JAR with bundled dependencies |
| `runServer` | Build and start development server |
| `build` | Compile and build all artifacts |
| `clean` | Delete build directory |
| `test` | Run unit tests |
| `compileJava` | Compile Java sources only |
| `dependencies` | Show dependency tree |
| `tasks` | List all available tasks |

---

## Task Combinations

```bash
# Clean and rebuild
./gradlew clean build

# Clean, test, and build
./gradlew clean test build

# Build with specific property
./gradlew shadowJar -Pversion=1.0.0

# Run with debug mode
./gradlew runServer -Pdebug

# Skip tests
./gradlew build -x test

# Show more output
./gradlew build --info

# Show even more output
./gradlew build --debug
```

---

## Benefits of Gradle Wrapper

1. **Version Consistency:** Everyone uses the same Gradle version
2. **No Installation Required:** Gradle doesn't need to be pre-installed
3. **CI/CD Friendly:** Build servers work out of the box
4. **Reproducible Builds:** Same version = same behavior
5. **Easy Updates:** Just change `gradle-wrapper.properties`

---

## Troubleshooting

### Permission Denied (Unix)

```bash
chmod +x gradlew
```

### Wrapper Fails to Download

Check your internet connection and proxy settings. You can also manually download Gradle and configure it:

```bash
# Set Gradle home manually
export GRADLE_USER_HOME=/path/to/gradle
```

### Wrong Java Version

```bash
# Check Java version
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/jdk-25
```

### Corrupted Cache

```bash
# Clear Gradle cache
rm -rf ~/.gradle/wrapper/dists/
rm -rf ~/.gradle/caches/

# Run again
./gradlew build
```

---

## Related Files

- [gradle-wrapper.properties.md](gradle/wrapper/gradle-wrapper.properties.md) - Wrapper configuration
- [build.gradle.kts.md](build.gradle.kts.md) - Build configuration

## In-Depth Overview


