package org.almond.lands.model;

public enum LandPermission {
    BUILD("Place blocks", false),
    BREAK("Break blocks", false),
    INTERACT("Use doors/levers", false),
    CONTAINER("Access chests", false),
    MANAGE_MEMBERS("Add/remove members", true),   // admin perm
    MANAGE_ROLES("Edit roles", true),             // admin perm
    CLAIM("Create regions", true),                // admin perm
    UNCLAIM("Delete regions", true);              // admin perm

    private final String description;
    private final boolean isAdmin;

    // Enum constructors are implicitly private
    LandPermission(String description, boolean isAdmin) {
        this.description = description;
        this.isAdmin = isAdmin;
    }

    public String getDescription() {
        return description;
    }
    public boolean isAdmin() {
        return isAdmin;
    }
}