package org.almond.lands.model;

import com.hypixel.hytale.math.vector.Vector3i;

public class Region {
    private Vector3i corner1;           // First corner (min)
    private Vector3i corner2;           // Second corner (max)

    /** Constructor to create a region from two corners */
    public Region(Vector3i corner1, Vector3i corner2){
        this.corner1 = new Vector3i(
            Math.min(corner1.getX(), corner2.getX()),
            Math.min(corner1.getY(), corner2.getY()),
            Math.min(corner1.getZ(), corner2.getZ())
        );
        this.corner2 = new Vector3i(
            Math.max(corner1.getX(), corner2.getX()),
            Math.max(corner1.getY(), corner2.getY()),
            Math.max(corner1.getZ(), corner2.getZ())
        );
    };
    
    /** Checks if the given position is within the region */
    public boolean contains(Vector3i pos){
        return pos.getX() >= corner1.getX() && pos.getX() <= corner2.getX() &&
               pos.getY() >= corner1.getY() && pos.getY() <= corner2.getY() &&
               pos.getZ() >= corner1.getZ() && pos.getZ() <= corner2.getZ();
    };

    /** Checks if this region is adjacent, including overlapping, to another region */
    public boolean isAdjacentTo(Region other){

        boolean overlap = this.corner1.getX() <= other.corner2.getX() && this.corner2.getX() >= other.corner1.getX() &&
                          this.corner1.getY() <= other.corner2.getY() && this.corner2.getY() >= other.corner1.getY() &&
                          this.corner1.getZ() <= other.corner2.getZ() && this.corner2.getZ() >= other.corner1.getZ();

        boolean xAdjacent = (this.corner2.getX() + 1 == other.corner1.getX() || this.corner1.getX() - 1 == other.corner2.getX()) &&
                            (this.corner1.getY() <= other.corner2.getY() && this.corner2.getY() >= other.corner1.getY()) &&
                            (this.corner1.getZ() <= other.corner2.getZ() && this.corner2.getZ() >= other.corner1.getZ());
        
        boolean yAdjacent = (this.corner2.getY() + 1 == other.corner1.getY() || this.corner1.getY() - 1 == other.corner2.getY()) &&
                            (this.corner1.getX() <= other.corner2.getX() && this.corner2.getX() >= other.corner1.getX()) &&
                            (this.corner1.getZ() <= other.corner2.getZ() && this.corner2.getZ() >= other.corner1.getZ());
        
        boolean zAdjacent = (this.corner2.getZ() + 1 == other.corner1.getZ() || this.corner1.getZ() - 1 == other.corner2.getZ()) &&
                            (this.corner1.getX() <= other.corner2.getX() && this.corner2.getX() >= other.corner1.getX()) &&
                            (this.corner1.getY() <= other.corner2.getY() && this.corner2.getY() >= other.corner1.getY());
        
        return xAdjacent || yAdjacent || zAdjacent || overlap;
    };

    /** Calculates the volume of the region */
    public long getVolume(){
        long length = corner2.getX() - corner1.getX() + 1;
        long width = corner2.getY() - corner1.getY() + 1;
        long height = corner2.getZ() - corner1.getZ() + 1;
        return length * width * height;
    };
}