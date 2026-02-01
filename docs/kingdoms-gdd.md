# Kingdoms - Game Design Document

## Overview

**Genre:** Co-op Persistent RTS  
**Players:** 1-4 players vs AI  
**Inspiration:** Stronghold Kingdoms  
**Platform:** Web (browser)  
**Session Duration:** Weeks (persistent world)

## Core Concept

A medieval building and warfare game where players collaborate against an AI that develops progressively. Build bases, train armies, and fight to destroy all AI bases.

### Key Features

- **Co-op vs AI** - Players collaborate, not compete
- **Persistent World** - The game continues even when you're offline
- **Auto-Defense** - Your army defends automatically when you're offline
- **Progressive Difficulty** - AI develops naturally over time
- **Production Chains** - Economy based on production chains (like Stronghold)

## Map & Territory

- **Large, open map**
- **No predefined territories** - build anywhere
- **Restriction:** Cannot build while under attack
- **Fog of War** - unexplored areas are hidden
- **Shared Vision** - allied players share map visibility

---

## Game Session

### Lobby System
- Players can **create new games** in a lobby
- Game creator selects a **predefined map**
- Other players can **browse and join** available games
- **Join anytime** - new players can join mid-game if slots are available

### Game Start
- Each player **spawns in a different zone** of the map
- Starting resources: *(to be defined)*
- **AI bases are fixed** per map (predefined positions and count)

### Player Slots
- Maximum players defined per map (1-4)
- Late joiners start fresh (no catch-up bonus)

---

## Bases

### Player Bases
- Each player has **their own separate base**
- Free positioning on map
- Can be expanded in any direction
- **Town Hall destroyed = player eliminated** (all buildings and units disappear)

### AI Bases
- **Multiple bases** distributed across the map
- **Identical to players** - same buildings, units, economy
- Develop over time (new buildings, larger armies)
- Launch **periodic attacks** on players
- Increasing difficulty as the game progresses

---

## Buildings

**No tech tree** - all buildings are available from the start if you have the resources.

### 🏛️ Main

| Building | Function |
|----------|----------|
| Town Hall | Base center, unlocks other buildings |

### ⛏️ Resource Extraction

| Building | Produces |
|----------|----------|
| Iron Mine | Iron |
| Stone Mine | Stone |
| Lumbermill | Wood |

### 🍖 Food Production

| Building | Input | Output |
|----------|-------|--------|
| Pig Farm | - | Meat |
| Cow Farm | - | Milk |
| Dairy | Milk | Cheese |
| Apple Orchard | - | Apples |
| Cherry Orchard | - | Cherries |
| Wheat Field | - | Wheat |
| Wheat Mill | Wheat | Flour |
| Bakery | Flour | Bread |

### 📦 Storage

| Building | Stores |
|----------|--------|
| Food Storage | All food types |
| Raw Material Storage | Wood, stone, iron |
| Weapons Storage | Weapons and armor |

### ⚔️ Weapons Production

| Building | Input | Output |
|----------|-------|--------|
| Bow Workshop | Wood | Bows |
| Spear Workshop | Wood, Iron | Spears |
| Armor Workshop | Iron | Armor |
| Sword Workshop | Iron | Swords |
| Siege Workshop | Wood, Iron | Rams, Catapults |

### 🏠 Population

| Building | Function |
|----------|----------|
| Houses | Increase population cap (workers + soldiers) |
| Barracks | Shelter soldiers, training |

**Population System** (like Stronghold):
- Houses determine **max population**
- Population is shared between workers and soldiers
- More houses = more units available

### 🛡️ Defensive

| Building | Function | Variants |
|----------|----------|----------|
| Tower | Defense, visibility | Wood, Stone |
| Wall | Defensive barrier (has HP, can be destroyed) | Wood, Stone |
| Gate | Controlled entry through walls | Wood, Stone |

---

## Economy Flow

```
┌─────────────┐
│  Town Hall  │
└──────┬──────┘
       │ trains
       ▼
┌─────────────┐
│   Workers   │
└──────┬──────┘
       │ produce
       ▼
┌─────────────────────────────────┐
│    Wood, Stone, Iron, Food     │
└──────┬──────────────────────────┘
       │
       ▼
┌─────────────┐     ┌─────────────┐
│    Iron     │────▶│  Workshops  │
└─────────────┘     └──────┬──────┘
                           │ produce
                           ▼
                   ┌───────────────┐
                   │  Bows/Swords/ │
                   │ Spears/Armor  │
                   └───────┬───────┘
                           │
       ┌───────────────────┤
       ▼                   ▼
┌─────────────┐     ┌─────────────┐
│    Food     │  +  │   Weapons   │
└──────┬──────┘     └──────┬──────┘
       │                   │
       └─────────┬─────────┘
                 ▼
         ┌─────────────┐
         │  Barracks   │
         └──────┬──────┘
                │ trains
                ▼
         ┌─────────────┐
         │  Soldiers   │
         └─────────────┘
```

## Production Chains

```
🌾 BREAD
Wheat Field → Mill → Bakery → Bread

🧀 CHEESE  
Cow Farm → Dairy → Cheese

⚔️ WEAPONS
Iron Mine → Workshops → Swords/Spears/Armor
Lumbermill → Workshops → Bows/Spears
```

---

## Resources

### Raw Materials

| Resource | Source | Use |
|----------|--------|-----|
| **Wood** | Lumbermill | Construction, weapons |
| **Stone** | Stone Mine | Fortifications |
| **Iron** | Iron Mine | Weapons, armor |

### Food

| Type | Source |
|------|--------|
| Meat | Pig Farm |
| Milk | Cow Farm |
| Cheese | Dairy (from milk) |
| Apples | Apple Orchard |
| Cherries | Cherry Orchard |
| Bread | Bakery (from flour) |

### Weapons

| Weapon | Ingredients |
|--------|-------------|
| Bow | Wood |
| Spear | Wood + Iron |
| Sword | Iron |
| Armor | Iron |

---

## Military Units

### Combat Units

| Unit | HP | DPS | Range | Required Equipment |
|------|-----|-----|-------|-------------------|
| **Archer** | 40 | 8 | 8 | Bow |
| **Swordsman** | 100 | 15 | melee | Sword |
| **Spearman** | 80 | 12 | melee | Spear |

### Siege Units

| Unit | HP | DPS | Range | Required Equipment | Note |
|------|-----|-----|-------|-------------------|------|
| **Battering Ram** | 200 | 30 vs buildings | melee | Wood | Slow, only damages walls/gates |
| **Catapult** | 80 | 25 (area) | 12 | Wood + Iron | Area damage to buildings and units |

### Combat System

- **No rock-paper-scissors** - no counters between units
- Each unit has **fixed DPS**
- What matters: number of units, positioning, target focus
- Archers have range advantage but are weak in melee

---

## Workers

- **Trained at Town Hall**
- **Build structures** when assigned to construction
- **Assigned to production buildings** - once assigned, they produce automatically (no micromanagement)
- No worker = building doesn't produce

---

## Upkeep (Food)

⚠️ **Food is critical!**

- **Everyone consumes food constantly** (workers + soldiers)
- No food = village starves
- Must balance: don't train more than you can feed

---

## Combat

- **Real-time battles**
- **Manual unit control** - select units and command them (move, attack)
- **Damage per second (DPS)** - each unit has its own DPS
- **Morale:** Units flee when army drops below 20% HP

### Auto-Defense (Offline)
- When offline, your army **defends automatically**
- Simple defensive AI: attacks enemies in range
- You receive a report when you reconnect

### Offline Production
- **Production continues** at the same rate when offline
- Resources accumulate in storage
- Workers keep working assigned buildings

---

## AI Attacks

- Come **periodically** (frequency increases over time)
- Intensity based on:
  - How developed your base is
  - How much time has passed in game
  - How many AI bases have been destroyed

---

## Victory Condition

🏆 **Destroy all AI bases**

Players win when the last AI base is eliminated.

---

## Game Flow

1. **Early Game** - Build base, set up production chains
2. **Mid Game** - Defend against AI attacks, produce weapons
3. **Late Game** - Coordinate attacks with other players on AI bases
4. **End Game** - Final assault on remaining AI bases

---

## Balancing

All numerical values will be defined in a dedicated `balancing.ts` config file:

- Building costs
- Production times
- Unit stats (HP, DPS, speed)
- Storage capacities
- Starting resources
- AI scaling parameters

This allows easy tweaking without modifying game logic.

---

*Document Version: 1.1*  
*Last Updated: 2026-01-27*
