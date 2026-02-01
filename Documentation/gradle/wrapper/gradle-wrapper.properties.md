# gradle-wrapper.properties

**Location:** `gradle/wrapper/gradle-wrapper.properties`  
**Type:** Java Properties File  
**Purpose:** Configures the Gradle Wrapper version and download settings

---

## Overview

This file tells the Gradle Wrapper which version of Gradle to download and use. It ensures all developers and build systems use exactly the same Gradle version.

---

## File Contents

```properties
#Tue Nov 25 02:23:10 MST 2025
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
distributionUrl=https\://services.gradle.org/distributions/gradle-9.2.0-bin.zip
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

---

## Property Reference

### distributionUrl

```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.2.0-bin.zip
```

| Aspect | Description |
|--------|-------------|
| **Purpose** | URL to download Gradle distribution |
| **Current Version** | 9.2.0 |
| **Distribution Type** | `-bin` (binary only, no source/docs) |

**Distribution Types:**
- `-bin.zip` - Binary only (smaller, faster download)
- `-all.zip` - Includes source code and documentation

### distributionBase / distributionPath

```properties
distributionBase=GRADLE_USER_HOME
distributionPath=wrapper/dists
```

| Property | Description |
|----------|-------------|
| `distributionBase` | Base directory (usually `~/.gradle`) |
| `distributionPath` | Subdirectory within base |

**Full path:** `~/.gradle/wrapper/dists/gradle-9.2.0-bin/...`

### zipStoreBase / zipStorePath

```properties
zipStoreBase=GRADLE_USER_HOME
zipStorePath=wrapper/dists
```

Where the downloaded ZIP file is stored before extraction. Usually the same as distribution paths.

---

## Gradle Version

This project uses **Gradle 9.2.0**, which provides:
- Java 25 support
- Kotlin DSL improvements
- Configuration cache
- Performance optimizations

---

## Updating Gradle Version

### Using Wrapper Task

```bash
# Update to specific version
./gradlew wrapper --gradle-version 9.3.0

# Update to latest
./gradlew wrapper --gradle-version latest
```

### Manual Update

Edit `distributionUrl` in this file:
```properties
distributionUrl=https\://services.gradle.org/distributions/gradle-9.3.0-bin.zip
```

**Note:** The `\:` escape is required for the colon in properties files.

---

## Verification

After updating, verify the version:
```bash
./gradlew --version
```

Expected output:
```
------------------------------------------------------------
Gradle 9.2.0
------------------------------------------------------------

Build time:   2025-XX-XX XX:XX:XX UTC
Revision:     XXXXXXXXXX

Kotlin:       X.X.X
Groovy:       X.X.X
Ant:          Apache Ant(TM) version X.X.X
JVM:          25.X.X (...)
OS:           Windows XX ...
```

---

## Troubleshooting

### Download Fails

**Behind Proxy:**
Create/edit `~/.gradle/gradle.properties`:
```properties
systemProp.http.proxyHost=proxy.example.com
systemProp.http.proxyPort=8080
systemProp.https.proxyHost=proxy.example.com
systemProp.https.proxyPort=8080
```

**SSL Issues:**
```properties
systemProp.https.protocols=TLSv1.2,TLSv1.3
```

### Corrupted Download

Delete cached distribution and re-run:
```bash
rm -rf ~/.gradle/wrapper/dists/gradle-9.2.0-bin/
./gradlew build
```

---

## Related Files

- [gradlew.md](../../gradlew.md) - Wrapper scripts
- [build.gradle.kts.md](../../build.gradle.kts.md) - Build configuration
