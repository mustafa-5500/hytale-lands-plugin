package org.almond.lands.manager;

import java.util.HashMap;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;
import java.util.Set;
import java.util.HashSet;
import java.lang.System;
import org.almond.lands.model.Land;
import org.almond.lands.model.Region;
import com.hypixel.hytale.math.vector.Vector3i;

public class LandManager {

    private Map<UUID, Land> landsById = new HashMap<>();
    private Map<String, Land> landsByName = new HashMap<>();
    private Map<UUID, UUID> selectedLandByPlayer = new HashMap<>();

    /** Creates a new land with default roles and adds it to the manager */
    public void createLand(String name, UUID ownerId, Region region) {
        UUID landId = UUID.randomUUID();
        Map<UUID, String> members = new HashMap<>();
        members.put(ownerId, "owner");
        Land land = new Land(landId, name, ownerId, List.of(region), members, null, System.currentTimeMillis());
        landsById.put(landId, land);
        landsByName.put(name, land);
    }

    /** Deletes a land by its name */
    public void deleteLand(String name) {
        Land land = getLandByName(name);
        if (land != null) {
            landsById.remove(land.getId());
            landsByName.remove(name);
        } else {
            throw new IllegalArgumentException("Land with name " + name + " does not exist.");
        }
    }

    /** Selects a land for a player by land name */
    public void selectLandForPlayer(UUID playerId, String landName) {
        if (landsByName.containsKey(landName)) {
            UUID landId = landsByName.get(landName).getId();
            selectedLandByPlayer.put(playerId, landId);
        } else {
            throw new IllegalArgumentException("Land with name " + landName + " does not exist.");
        }
    }

    /** Clears the selected land for a player */
    public void clearSelectedLandForPlayer(UUID playerId) {
        selectedLandByPlayer.remove(playerId);
    }

    /** Retrieves the selected land for a player */
    public Land getSelectedLandForPlayer(UUID playerId) {
        UUID landId = selectedLandByPlayer.get(playerId);
        if (landId != null) {
            return landsById.get(landId);
        }
        return null;
    }

    /** Claims region for a land */
    public void claimRegion(UUID playerId, Region newRegion) {
        Land land = getSelectedLandForPlayer(playerId);
        if (land != null) {
            List<Region> newRegions = new ArrayList<>();
            newRegions.add(newRegion);
            // Check for overlaps with existing regions
            for (Region region : land.getRegions()) {
                for (Region newReg : newRegions) {
                    if (region.overlaps(newReg)) {
                        // Split the new region into non-overlapping parts
                        List<Region> splitRegions = newReg.subtract(region);
                        newRegions.remove(newReg);
                        newRegions.addAll(splitRegions);
                    }
                }
            }
            land.claimRegions(newRegions);
            // Merge regions if necessary, to optimize storage and lookup (not implemented here for simplicity)
        } else {
            throw new IllegalArgumentException("No land selected for the player.");
        }
    }

    /** Retrieves a land that contains the given position */
    public Land getLandAt(Vector3i position) {
        for (Land land : landsById.values()) {
            for (Region region : land.getRegions()) {
                if (region.contains(position)) {
                    return land;
                }
            }
        }
        return null;
    }

    /** Retrieves a land by its unique identifier */
    public Land getLandById(UUID landId) {
        return landsById.get(landId);
    }

    /** Retrieves a land by its name */
    public Land getLandByName(String name) {
        return landsByName.get(name);
    }

    /** Retrieves a land by its owner's UUID */
    public Land getLandByOwner(UUID ownerId) {
        for (Land land : landsById.values()) {
            if (land.getOwner().equals(ownerId)) {
                return land;
            }
        }
        return null;
    }

    /** Returns all lands managed by this LandManager */
    public Set<Land> getAllLands() {
        return new HashSet<>(landsById.values());
    }
    
}