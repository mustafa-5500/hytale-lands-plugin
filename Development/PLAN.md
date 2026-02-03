# Hytale Lands Plugin - Development Plan

A land claiming and protection system for Hytale servers. Players can create lands, claim cuboid regions, and manage permissions through a role-based system.

**Created:** February 2, 2026  
**Status:** Planning Phase

---

## Table of Contents

1. [Overview](#overview)
2. [Core Features](#core-features)
3. [Architecture](#architecture)
4. [Data Models](#data-models)
5. [Commands](#commands)
6. [Events & Protection](#events--protection)
7. [Implementation Steps](#implementation-steps)
8. [Open Questions](#open-questions)
9. [Future Expansion](#future-expansion)

---

## Overview

The Lands plugin allows players to:
- Create named lands that they own
- Claim areas using cuboid region selection (two corners)
- Expand lands with additional regions (must be continuous)
- Control who can interact with their land via roles and permissions
- Trust other players with specific permission levels

---

## Core Features

### 1. Land Creation & Ownership
- Each land has a unique name and single owner
- Owner identified by UUID for persistence
- Lands can have multiple members with assigned roles

### 2. Cuboid Region Selection
- Players select two corners (pos1, pos2) to define a cuboid
- Selection via command (`/land pos1`, `/land pos2`) or selection tool
- Visual feedback on selection (if particle API available)

### 3. Continuous Region Requirement
- New regions must be adjacent to existing land regions
- Adjacency = sharing at least one block face (not just diagonal)
- First region has no adjacency requirement

### 4. Role-Based Permissions
- Lands have customizable roles (e.g., Member, Trusted, Moderator)
- Each role defines what actions are allowed
- Owner always has full permissions

### 5. Protection System
- Block breaking protection
- Block placing protection
- Block interaction protection (chests, doors, etc.)
- Entity interaction protection (future)

---

## Architecture

```
hytale-lands-plugin/
├── src/
│   └── main/
│       ├── java/
│       │   └── org/
│       │       └── almond/
│       │           └── lands/
│       │               ├── LandsPlugin.java         # Main plugin entry point
│       │               ├── command/
│       │               │   ├── LandCommand.java     # /land command handler
│       │               │   └── LandAdminCommand.java # /landadmin for operators
│       │               ├── model/
│       │               │   ├── Land.java            # Land data structure
│       │               │   ├── Region.java          # Cuboid region (two Vector3i)
│       │               │   ├── LandRole.java        # Role with permission flags
│       │               │   └── LandPermission.java  # Permission enum
│       │               ├── manager/
│       │               │   ├── LandManager.java     # Land CRUD & position lookup
│       │               │   └── SelectionManager.java # Player corner selections
│       │               ├── listener/
│       │               │   └── ProtectionListener.java # Block/interact event handlers
│       │               └── storage/
│       │                   └── LandStorage.java     # JSON file persistence
│       └── resources/
│           ├── manifest.json
│           ├── Common/                              # Assets (models, textures)
│           └── Server/                              # Server-side data
├── build.gradle
├── settings.gradle
├── gradle.properties
├── README.md
└── run/
```

---

## Data Models

### Land
```java
public class Land {
    private UUID id;                    // Unique land identifier
    private String name;                // Display name
    private UUID owner;                 // Owner's player UUID
    private List<Region> regions;       // Claimed cuboid regions
    private Map<UUID, String> members;  // Player UUID -> Role name
    private Map<String, LandRole> roles; // Role name -> Role definition
    private long createdAt;             // Timestamp
}
```

### Region
```java
public class Region {
    private Vector3i corner1;           // First corner (min)
    private Vector3i corner2;           // Second corner (max)
    
    public boolean contains(Vector3i pos);
    public boolean isAdjacentTo(Region other);
    public long getVolume();
}
```

### LandRole
```java
public class LandRole {
    private String name;                        // Role identifier
    private Set<LandPermission> permissions;    // Granted permissions
}
```

### LandPermission (Enum)
```java
public enum LandPermission {
    BUILD,          // Place blocks
    BREAK,          // Break blocks
    INTERACT,       // Use doors, buttons, levers
    CONTAINER,      // Access chests, furnaces
    MANAGE_MEMBERS, // Add/remove members
    MANAGE_ROLES,   // Create/edit roles
    CLAIM,          // Add new regions
    UNCLAIM         // Remove regions
}
```

---

## Commands

### Player Commands: `/land`

| Subcommand | Usage | Description |
|------------|-------|-------------|
| `create` | `/land create <name>` | Create a new land |
| `delete` | `/land delete <name>` | Delete your land |
| `claim` | `/land claim` | Claim selected region for current land |
| `unclaim` | `/land unclaim` | Unclaim region at current position |
| `pos1` | `/land pos1` | Set first corner to current position |
| `pos2` | `/land pos2` | Set second corner to current position |
| `trust` | `/land trust <player> [role]` | Add player to your land |
| `untrust` | `/land untrust <player>` | Remove player from your land |
| `role create` | `/land role create <name>` | Create a new role |
| `role delete` | `/land role delete <name>` | Delete a role |
| `role perm` | `/land role perm <role> <perm> <true/false>` | Set role permission |
| `list` | `/land list` | List your lands |
| `info` | `/land info [name]` | Show land info |
| `select` | `/land select <name>` | Select active land for claiming |

### Admin Commands: `/landadmin`

| Subcommand | Usage | Description |
|------------|-------|-------------|
| `bypass` | `/landadmin bypass` | Toggle protection bypass |
| `delete` | `/landadmin delete <land>` | Force delete any land |
| `info` | `/landadmin info <land>` | View any land's details |
| `reload` | `/landadmin reload` | Reload configuration |

---

## Events & Protection

### Protected Events

| Event | Permission Required | Cancel Condition |
|-------|---------------------|------------------|
| `BreakBlockEvent` | `BREAK` | Player lacks permission in land |
| `PlaceBlockEvent` | `BUILD` | Player lacks permission in land |
| `DamageBlockEvent` | `BREAK` | Player lacks permission in land |
| `PlayerInteractEvent` | `INTERACT` / `CONTAINER` | Player lacks permission in land |

### Protection Logic
```
1. Get block position from event
2. Find land containing this position (LandManager.getLandAt)
3. If no land → allow action (wilderness)
4. If land exists:
   a. Check if player is owner → allow
   b. Check if player has admin bypass → allow
   c. Get player's role in land
   d. Check if role has required permission
   e. Allow or cancel based on permission check
```

---

## Implementation Steps

### Phase 1: Foundation
- [✓] Create package structure
- [✓] Implement `Region` class with containment/adjacency logic
- [✓] Implement `LandPermission` enum
- [✓] Implement `LandRole` class
- [✓] Implement `Land` class

### Phase 2: Managers
- [ ] Implement `SelectionManager` (player corner tracking)
- [ ] Implement `LandManager` (CRUD, position lookup)
- [ ] Add chunk-based spatial indexing for performance

### Phase 3: Persistence
- [ ] Implement `LandStorage` with JSON serialization
- [ ] Add save on shutdown
- [ ] Add load on startup
- [ ] Add auto-save interval (optional)

### Phase 4: Commands
- [ ] Create base `LandCommand` with subcommand routing
- [ ] Implement `create`, `delete` subcommands
- [ ] Implement `pos1`, `pos2`, `claim`, `unclaim` subcommands
- [ ] Implement `trust`, `untrust` subcommands
- [ ] Implement `role` subcommands
- [ ] Implement `list`, `info` subcommands
- [ ] Create `LandAdminCommand`

### Phase 5: Protection
- [ ] Create `ProtectionListener`
- [ ] Register block break protection
- [ ] Register block place protection
- [ ] Register interaction protection
- [ ] Add permission checking logic

### Phase 6: Polish
- [ ] Add user-friendly messages
- [ ] Add tab completion for commands
- [ ] Add configuration file for defaults
- [ ] Testing and bug fixes

---

## Open Questions

### 1. Adjacency Definition
**Question:** Should diagonal adjacency count as continuous?  
**Current Decision:** Face-adjacent only (simpler, clearer boundaries)  
**Alternative:** Allow edge or corner adjacency for more flexibility

### 2. Claim Limits
**Question:** Should players have limits on claims?  
**Options:**
- Maximum total volume (e.g., 10,000 blocks)
- Maximum number of regions per land
- Maximum number of lands per player
- Permission-based limits (`lands.maxblocks.X`)

**Current Decision:** TBD - implement without limits first, add later

### 3. Default Wilderness Permissions
**Question:** What can non-members do in unclaimed land?  
**Current Decision:** Everything (no protection in wilderness)

### 4. Default Land Permissions
**Question:** What can non-members do in someone's land?  
**Current Decision:** Nothing (full protection by default)

### 5. Selection Tool
**Question:** Use item-based selection or command-only?  
**Options:**
- Command only (`/land pos1`, `/land pos2`)
- Dedicated item (e.g., golden shovel)
- Both

**Current Decision:** Commands first, item-based later

---

## Future Expansion

These features are not in the initial scope but planned for future versions:

### v1.1 - Enhanced Claiming
- [ ] Selection tool item
- [ ] Visual boundary particles
- [ ] Claim cost (economy integration)
- [ ] Rent system for temporary access

### v1.2 - Advanced Protection
- [ ] Entity protection (animals, item frames)
- [ ] PvP toggle per land
- [ ] Mob spawning control
- [ ] Explosion protection

### v1.3 - Social Features
- [ ] Land chat channel
- [ ] Land spawn point / home
- [ ] Public/private land toggle
- [ ] Land leaderboard

### v1.4 - Administration
- [ ] Web map integration
- [ ] Inactive land expiration
- [ ] Land taxation
- [ ] Audit logging

### v1.5 - Integration
- [ ] Economy plugin support
- [ ] Dynmap/web map markers
- [ ] API for other plugins

---

## Technical Notes

### Hytale API References

| Component | API Class/Package |
|-----------|-------------------|
| Positions | `com.hypixel.hytale.math.Vector3i` |
| Players | `com.hypixel.hytale.server.core.universe.PlayerRef` |
| Events | `com.hypixel.hytale.server.core.event` |
| Commands | `com.hypixel.hytale.server.core.command.system.basecommands.CommandBase` |
| Messages | `com.hypixel.hytale.server.core.Message` |
| Logging | `com.hypixel.hytale.logger.HytaleLogger` |
| Storage | `JavaPlugin.getDataDirectory()` |

### Permission Nodes

```
lands.create          - Create new lands
lands.claim           - Claim regions
lands.trust           - Add members to lands
lands.role.manage     - Create/edit roles
lands.admin           - Admin override
lands.admin.bypass    - Bypass all protection
lands.admin.delete    - Delete any land
lands.maxlands.X      - Maximum lands (X = number)
lands.maxvolume.X     - Maximum claim volume (X = blocks)
```

---

## Changelog

| Date | Change |
|------|--------|
| 2026-02-02 | Initial plan created |
