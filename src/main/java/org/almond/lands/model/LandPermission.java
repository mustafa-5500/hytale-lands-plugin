package org.almond.lands.model;

public enum LandPermission {
    BUILD("Place blocks"),
    BREAK("Break blocks"),
    INTERACT("Use doors/levers"),
    CONTAINER("Access chests"),
    MANAGE_MEMBERS("Add/remove members"),
    MANAGE_ROLES("Edit roles"),
    CLAIM("Create regions"),
    UNCLAIM("Delete regions");

    private final String description;

    // Enum constructors are implicitly private
    LandPermission(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}