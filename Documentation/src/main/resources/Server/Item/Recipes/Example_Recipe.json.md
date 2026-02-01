# Example_Recipe.json

**Location:** `src/main/resources/Server/Item/Recipes/Example_Recipe.json`  
**Type:** JSON Asset File  
**Purpose:** Demonstrates how to add custom crafting recipes to Hytale

---

## Overview

This file defines a custom crafting recipe that can be used at a crafting bench in-game. It's an example of how plugins with `IncludesAssetPack: true` can add game content without writing code.

---

## File Contents

```json
{
  "Input": [
    {
      "ItemId": "Soil_Dirt",
      "Quantity": 10
    }
  ],
  "PrimaryOutput": {
    "ItemId": "Soil_Dirt",
    "Quantity": 1
  },
  "BenchRequirement": [
    {
      "Id": "Fieldcraft",
      "Type": "Crafting",
      "Categories": [
        "Tools"
      ]
    }
  ],
  "Seconds": 1
}
```

---

## Field Reference

### Input

```json
"Input": [
    {
        "ItemId": "Soil_Dirt",
        "Quantity": 10
    }
]
```

| Field | Description |
|-------|-------------|
| **Input** | Array of required ingredients |
| **ItemId** | Game item identifier |
| **Quantity** | Amount needed |

**Multiple inputs example:**
```json
"Input": [
    { "ItemId": "Wood_Plank", "Quantity": 4 },
    { "ItemId": "Iron_Ingot", "Quantity": 2 }
]
```

### PrimaryOutput

```json
"PrimaryOutput": {
    "ItemId": "Soil_Dirt",
    "Quantity": 1
}
```

| Field | Description |
|-------|-------------|
| **PrimaryOutput** | The main item produced |
| **ItemId** | Output item identifier |
| **Quantity** | Amount produced |

**Note:** This example is intentionally simple (10 dirt → 1 dirt) for demonstration. Real recipes would produce different/useful items.

### BenchRequirement

```json
"BenchRequirement": [
    {
        "Id": "Fieldcraft",
        "Type": "Crafting",
        "Categories": [
            "Tools"
        ]
    }
]
```

| Field | Description |
|-------|-------------|
| **BenchRequirement** | Array of crafting station requirements |
| **Id** | Crafting station identifier |
| **Type** | Station type (usually "Crafting") |
| **Categories** | Recipe category tabs |

**Crafting Station IDs:**
- `"Fieldcraft"` - Basic field crafting table
- `"Forge"` - Metalworking station
- `"Alchemy"` - Potion brewing
- (Others depend on game content)

**Categories:**
Categories determine which tab the recipe appears under:
- `"Tools"` - Tool recipes
- `"Weapons"` - Weapon recipes
- `"Armor"` - Armor recipes
- `"Building"` - Construction materials
- (Custom categories can be created)

### Seconds

```json
"Seconds": 1
```

| Field | Description |
|-------|-------------|
| **Seconds** | Time to craft in seconds |
| **Range** | 0+ (instant to very long) |

Setting this to `1` means the recipe completes in one second.

---

## Recipe Types

### Basic Recipe (shown above)

Simple input → output transformation.

### Recipe with Secondary Output

```json
{
  "Input": [
    { "ItemId": "Ore_Iron", "Quantity": 3 }
  ],
  "PrimaryOutput": {
    "ItemId": "Iron_Ingot",
    "Quantity": 1
  },
  "SecondaryOutput": {
    "ItemId": "Slag",
    "Quantity": 1,
    "Chance": 0.5
  },
  "BenchRequirement": [
    { "Id": "Forge", "Type": "Crafting", "Categories": ["Smelting"] }
  ],
  "Seconds": 5
}
```

### Recipe with Multiple Categories

```json
{
  "BenchRequirement": [
    {
      "Id": "Fieldcraft",
      "Type": "Crafting",
      "Categories": ["Tools", "Survival"]
    }
  ]
}
```

Recipe appears in both "Tools" and "Survival" tabs.

---

## File Naming

Recipe files should be named descriptively:

| Pattern | Example |
|---------|---------|
| `Item_Recipe.json` | `Iron_Sword_Recipe.json` |
| `Category_Item.json` | `Tool_Pickaxe.json` |
| `ModName_Item.json` | `MyMod_CustomSword.json` |

**Avoid:**
- Spaces in filenames
- Special characters
- Very long names

---

## Directory Structure

Recipes must be in the correct path to be loaded:

```
src/main/resources/
└── Server/
    └── Item/
        └── Recipes/
            ├── Example_Recipe.json
            ├── MyCustom_Recipe.json
            └── AnotherRecipe.json
```

The path `Server/Item/Recipes/` is required by Hytale's asset loading system.

---

## Common Item IDs

Here are some vanilla Hytale item IDs for reference:

| Category | Examples |
|----------|----------|
| **Soils** | `Soil_Dirt`, `Soil_Sand`, `Soil_Gravel` |
| **Woods** | `Wood_Oak`, `Wood_Plank`, `Wood_Stick` |
| **Metals** | `Ore_Iron`, `Iron_Ingot`, `Iron_Nugget` |
| **Tools** | `Tool_Pickaxe_Stone`, `Tool_Axe_Iron` |

**Finding IDs:** Check Hytale's asset files or documentation for the complete list.

---

## Creating Custom Recipes

### Step 1: Create the JSON file

```json
{
  "Input": [
    { "ItemId": "Wood_Plank", "Quantity": 2 }
  ],
  "PrimaryOutput": {
    "ItemId": "Wood_Stick",
    "Quantity": 4
  },
  "BenchRequirement": [
    { "Id": "Fieldcraft", "Type": "Crafting", "Categories": ["Materials"] }
  ],
  "Seconds": 2
}
```

### Step 2: Save in correct location

Save as `src/main/resources/Server/Item/Recipes/Your_Recipe.json`

### Step 3: Ensure IncludesAssetPack is true

In `gradle.properties`:
```properties
includes_pack=true
```

### Step 4: Build and test

```bash
./gradlew runServer
```

---

## Validation

Check your recipe for:

- [ ] Valid JSON syntax (no trailing commas, proper quotes)
- [ ] Valid ItemIds (exist in game)
- [ ] Correct file path
- [ ] Positive quantities
- [ ] Reasonable crafting time
- [ ] Valid bench and categories

---

## Troubleshooting

### Recipe doesn't appear

1. Check `IncludesAssetPack: true` in manifest
2. Verify file path is exactly right
3. Check JSON syntax
4. Verify ItemIds are valid

### Recipe crashes server

1. Check for invalid ItemIds
2. Verify JSON syntax
3. Check server logs for errors

### Recipe in wrong category

1. Update `Categories` array
2. Verify category names are correct

---

## Related Files

- [manifest.json.md](../../manifest.json.md) - Must have `IncludesAssetPack: true`
- [gradle.properties.md](../../../../../gradle.properties.md) - `includes_pack` setting
