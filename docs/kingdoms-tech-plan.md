# Kingdoms - Technical Plan

## Tech Stack

### Frontend
- **Framework:** React 19 + TypeScript
- **3D Rendering:** React Three Fiber + Three.js
- **Camera:** Isometric
- **State:** Colyseus client SDK (server state) + Jotai (global/UI state) + Valtio (reactive game state)
- **UI:** Tailwind CSS + shadcn/ui
- **Hosting:** Vercel

### Backend
- **Framework:** Colyseus (multiplayer game server)
- **Runtime:** Node.js
- **Persistence:** JSON files
- **Hosting:** Hetzner VPS + Coolify
- **CI/CD:** GitHub Actions → Coolify auto-deploy

### Shared
- **Monorepo:** Turborepo + pnpm
- **Validation:** Zod (shared schemas)
- **Config:** Shared balancing config package

---

## Architecture

### Server-Authoritative
- Server controls all game logic
- Client sends commands only (place building, move unit, attack)
- Server validates and executes
- Prevents cheating

### Game Loop
- **Tick rate:** 5 ticks/second
- Colyseus syncs state to clients automatically
- Production/combat calculated server-side each tick

### Persistence
- Game state saved periodically to JSON files
- **Server runs 24/7** even when no players online
- On reconnect, client receives current state

### State Management

| Layer | Library | Purpose |
|-------|---------|---------|
| Server sync | Colyseus | Authoritative game state from server |
| Global/UI | Jotai | Settings, UI state, non-game data |
| Reactive game | Valtio | Client-side game state that needs reactivity |

---

## Networking

### Client-Server Communication

**Client → Server (Commands only):**
- `placeBuilding(type, x, y)`
- `moveUnits(unitIds[], targetX, targetY)`
- `attackTarget(unitIds[], targetId)`
- `trainUnit(buildingId, unitType)`
- `assignWorker(workerId, buildingId)`

**Server → Client:**
- State updates via Colyseus Schema (automatic delta sync)
- Events/notifications (attack alerts, etc.) - future

### Client-Side Prediction

| Action | Prediction | Behavior |
|--------|-----------|----------|
| Building placement | ✅ Yes | Instant "ghost" preview → confirm/reject from server |
| Unit movement | ✅ Yes | Units move immediately, server reconciles position |
| Attack | ⚠️ Partial | Unit orients toward target, damage calculated on server |
| Train unit | ❌ No | Only UI progress bar, server controls timing |

**Reconciliation:** At 5 ticks/sec (200ms), minor teleportation on correction is acceptable.

### Reconnection

- **Timeout:** 5 minutes
- Colyseus native reconnect: `room.reconnect(roomId, sessionId)`
- Server holds player slot during timeout
- On reconnect → full state sync automatically
- After timeout expires → player marked as disconnected (can rejoin anytime as "fresh connect")

### Unit Behavior (Connection Independent)

Units behave the same whether player is online or offline:
- Commands given are executed regardless of connection status
- **Auto-defense always active:** units automatically retaliate when attacked
- No difference between "online" and "offline" unit behavior
- Player gives commands (move, attack) → units execute → auto-defend if attacked en route

### Scale & Limits

| Metric | Limit |
|--------|-------|
| Units per player | 50-100 max |
| Units per AI base | ~50 |
| Total entities (units + buildings) | ~500 max |
| Area of Interest | Not needed for MVP (full state sync OK at this scale) |

---

## Technical Decisions

| Aspect | Decision |
|--------|----------|
| Rendering | 3D (React Three Fiber + Three.js) |
| Camera | Isometric |
| Building placement | Free (not grid-based) |
| Colliders | Spherical (units and buildings) |
| Pathfinding | Flowfields |
| Tick rate | 5/sec |
| Server uptime | Always-on (24/7) |
| Assets (MVP) | Placeholder shapes |

---

## Project Structure

```
games-studio/
├── apps/
│   └── kingdoms/              # React + R3F client
├── packages/
│   ├── kingdoms-core/         # Shared types, balancing config
│   ├── kingdoms-server/       # Colyseus server
│   │   ├── rooms/
│   │   │   ├── LobbyRoom.ts
│   │   │   └── GameRoom.ts
│   │   └── state/
│   │       └── GameState.ts
│   ├── ui/                    # Shared UI components
│   └── utils/                 # Shared utilities
└── ...
```

---

## Infrastructure

### Hosting Setup

```
┌─────────────────┐     ┌─────────────────┐
│ Vercel          │     │ Hetzner VPS     │
│                 │     │ + Coolify       │
│                 │     │                 │
│ Frontend (React)│────▶│ Colyseus Server │
│ Static assets   │ WS  │ Game state      │
└─────────────────┘     └─────────────────┘
```

### CI/CD Pipeline

```
GitHub Push → GitHub Actions → Coolify Webhook → Auto Deploy (backend)
GitHub Push → Vercel Auto Deploy (frontend)
```

- Push to `main` triggers deploy
- Coolify handles backend build + restart
- Vercel handles frontend automatically
- Zero-downtime deploys

---

## Game Rooms (Colyseus)

### Lobby Room
- List available games
- Create/join game
- Player slots management
- Pre-game chat

### Game Room
- Main game state (players, buildings, units, AI)
- Handles player commands
- Runs game tick loop (5/sec)
- Manages AI logic
- Persists state periodically

---

## Key Systems

### Economy
- Production calculated per-tick on server
- Workers assigned to buildings → output accumulates
- Storage limits enforced server-side

### Combat
- **Pathfinding:** Flowfields (efficient for unit groups)
- **Collisions:** Spherical colliders
- Damage calculation per-tick
- Unit commands: move, attack, stop

### AI
- Runs on same server tick
- Behavior tree or state machine
- Difficulty scales with game time + player strength

### Offline Play
- Server continues running ticks 24/7
- Auto-defense AI for player units
- Attack reports generated for offline events

---

## Development Phases

| Phase | Focus |
|-------|-------|
| 0 | Infrastructure: Colyseus setup, Coolify deploy, basic state sync |
| 1 | Core: Buildings, workers, resources, storage |
| 2 | Combat: Units, pathfinding (flowfields), attack system |
| 3 | AI: Enemy bases, attack waves, scaling difficulty |
| 4 | Persistence: Save/restore, offline systems, reports |
| 5 | Multiplayer: Lobby, late join, shared vision |
| 6 | Polish: Balancing, UI, playtesting |

---

## Open Questions (to be decided later)

1. **Fog of war implementation** - shader-based or geometry?
2. **Map editor** - built-in or external tool?
3. **Asset style** - low-poly, stylized, or realistic? (after placeholder phase)
4. **Sound** - library choice (Howler.js, Three.js audio, etc.)

---

*Document Version: 1.2*
*Last Updated: 2026-01-28*
