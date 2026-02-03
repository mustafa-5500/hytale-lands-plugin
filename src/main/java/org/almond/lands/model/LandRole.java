package org.almond.lands.model;

import java.util.Set;
import org.almond.lands.model.LandPermission;

public class LandRole {
    private String name;                        // Role identifier
    private Set<LandPermission> permissions;    // Granted permissions

    /** Constructor to create a land role with a name and permissions */
    public LandRole(String name, Set<LandPermission> permissions){
        this.name = name;
        this.permissions = permissions;
    };
}