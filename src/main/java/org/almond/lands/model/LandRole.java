package org.almond.lands.model;

import java.util.Set;
import org.almond.lands.model.LandPermission;

public class LandRole {
    private String name;                        // Role identifier
    private Set<LandPermission> permissions;    // Granted permissions
    private boolean isAdmin;                    // Whether this role has admin permissions

    /** Constructor to create a land role with a name and permissions */
    public LandRole(String name, Set<LandPermission> permissions){
        this.name = name;
        this.permissions = permissions;
        this.isAdmin = permissions.stream().anyMatch(LandPermission::isAdmin);
    };

    /** Getters */
    public String getName() {
        return name;
    }

    public Set<LandPermission> getPermissions() {
        return permissions;
    }

    public boolean isAdmin() {
        return isAdmin;
    }
}