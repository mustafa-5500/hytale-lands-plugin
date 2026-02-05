package org.almond.lands.model;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import com.hypixel.hytale.math.vector.Vector3i;

public class Region {
    private Vector3i corner1;           // First corner (min)
    private Vector3i corner2;           // Second corner (max)
    private Set<Region> adjacentRegions = new HashSet<>(); // Adjacent regions

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
        this.adjacentRegions = new HashSet<>();
    };
    
    /** Checks if the given position is within the region */
    public boolean contains(Vector3i pos){
        return pos.getX() >= corner1.getX() && pos.getX() <= corner2.getX() &&
               pos.getY() >= corner1.getY() && pos.getY() <= corner2.getY() &&
               pos.getZ() >= corner1.getZ() && pos.getZ() <= corner2.getZ();
    };

    /** Checks if this region is adjacent, including overlapping, to another region */
    public boolean isAdjacentTo(Region other){

        boolean xAdjacent = (this.corner2.getX() + 1 == other.corner1.getX() || this.corner1.getX() - 1 == other.corner2.getX()) &&
                            (this.corner1.getY() <= other.corner2.getY() && this.corner2.getY() >= other.corner1.getY()) &&
                            (this.corner1.getZ() <= other.corner2.getZ() && this.corner2.getZ() >= other.corner1.getZ());
        
        boolean yAdjacent = (this.corner2.getY() + 1 == other.corner1.getY() || this.corner1.getY() - 1 == other.corner2.getY()) &&
                            (this.corner1.getX() <= other.corner2.getX() && this.corner2.getX() >= other.corner1.getX()) &&
                            (this.corner1.getZ() <= other.corner2.getZ() && this.corner2.getZ() >= other.corner1.getZ());
        
        boolean zAdjacent = (this.corner2.getZ() + 1 == other.corner1.getZ() || this.corner1.getZ() - 1 == other.corner2.getZ()) &&
                            (this.corner1.getX() <= other.corner2.getX() && this.corner2.getX() >= other.corner1.getX()) &&
                            (this.corner1.getY() <= other.corner2.getY() && this.corner2.getY() >= other.corner1.getY());
        
        return xAdjacent || yAdjacent || zAdjacent;
    };

    /** Checks if this region overlaps with another region */
    public boolean overlaps(Region other){
        return this.corner1.getX() <= other.corner2.getX() && this.corner2.getX() >= other.corner1.getX() &&
               this.corner1.getY() <= other.corner2.getY() && this.corner2.getY() >= other.corner1.getY() &&
               this.corner1.getZ() <= other.corner2.getZ() && this.corner2.getZ() >= other.corner1.getZ();
    };

    /** Calculates the intersection region with another region, or null if none */
    public Region intersection(Region other){
        if (!this.overlaps(other)) {
            return null; // No intersection
        }
        Vector3i newCorner1 = new Vector3i(
            Math.max(this.corner1.getX(), other.corner1.getX()),
            Math.max(this.corner1.getY(), other.corner1.getY()),
            Math.max(this.corner1.getZ(), other.corner1.getZ())
        );
        Vector3i newCorner2 = new Vector3i(
            Math.min(this.corner2.getX(), other.corner2.getX()),
            Math.min(this.corner2.getY(), other.corner2.getY()),
            Math.min(this.corner2.getZ(), other.corner2.getZ())
        );
        return new Region(newCorner1, newCorner2);
    };

    /** Subtracts another region from this one and returns the resulting regions */
    public Set<Region> subtract(Region other){
        if (!this.overlaps(other)) {
            return Set.of(this); // No overlap, return this region as is
        }
        Region intersection = this.intersection(other);
        // Calculate the 6 potential remaining regions after subtraction
        Set<Region> remainingRegions = new HashSet<>();
        // Left region
        if (this.corner1.getX() < intersection.getCorner1().getX()) {
            remainingRegions.add(new Region(
                new Vector3i(this.corner1.getX(), this.corner1.getY(), this.corner1.getZ()),
                new Vector3i(intersection.getCorner1().getX() - 1, this.corner2.getY(), this.corner2.getZ())
            ));
        }
        // Right region
        if (this.corner2.getX() > intersection.getCorner2().getX()) {
            remainingRegions.add(new Region(
                new Vector3i(intersection.getCorner2().getX() + 1, this.corner1.getY(), this.corner1.getZ()),
                new Vector3i(this.corner2.getX(), this.corner2.getY(), this.corner2.getZ())
            ));
        }
        // Bottom region
        if (this.corner1.getY() < intersection.getCorner1().getY()) {
            remainingRegions.add(new Region(
                new Vector3i(intersection.getCorner1().getX(), this.corner1.getY(), this.corner1.getZ()),
                new Vector3i(intersection.getCorner2().getX(), intersection.getCorner1().getY() - 1, this.corner2.getZ())
            ));
        }
        // Top region
        if (this.corner2.getY() > intersection.getCorner2().getY()) {
            remainingRegions.add(new Region(
                new Vector3i(intersection.getCorner1().getX(), intersection.getCorner2().getY() + 1, this.corner1.getZ()),
                new Vector3i(intersection.getCorner2().getX(), this.corner2.getY(), this.corner2.getZ())
            ));
        }
        // Front region
        if (this.corner1.getZ() < intersection.getCorner1().getZ()) {
            remainingRegions.add(new Region(
                new Vector3i(intersection.getCorner1().getX(), intersection.getCorner1().getY(), this.corner1.getZ()),
                new Vector3i(intersection.getCorner2().getX(), intersection.getCorner2().getY(), intersection.getCorner1().getZ() - 1)
            ));
        }
        // Back region
        if (this.corner2.getZ() > intersection.getCorner2().getZ()) {
            remainingRegions.add(new Region(
                new Vector3i(intersection.getCorner1().getX(), intersection.getCorner1().getY(), intersection.getCorner2().getZ() + 1),
                new Vector3i(intersection.getCorner2().getX(), intersection.getCorner2().getY(), this.corner2.getZ())
            ));
        }

        // Update adjacent regions among remaining regions
        for (Region region1 : remainingRegions) {
            for (Region region2 : remainingRegions) {
                if (region1 != region2 && region1.isAdjacentTo(region2)) {
                    region1.adjacentRegions.add(region2);
                }
            }
        }

        // Update adjacent regions to include those from the original region
        for (Region adj : this.adjacentRegions) {
            for (Region region : remainingRegions) {
                if (region.isAdjacentTo(adj)) {
                    region.adjacentRegions.add(adj);
                }
            }
        }

        return remainingRegions;
    }

    /** Checks if two Regions share the same plane along 2 axis */
    public boolean isSamePlaneAs(Region other){
        boolean xSame = this.corner1.getY() == other.corner1.getY() && this.corner2.getY() == other.corner2.getY() &&
                        this.corner1.getZ() == other.corner1.getZ() && this.corner2.getZ() == other.corner2.getZ();
        
        boolean ySame = this.corner1.getX() == other.corner1.getX() && this.corner2.getX() == other.corner2.getX() &&
                        this.corner1.getZ() == other.corner1.getZ() && this.corner2.getZ() == other.corner2.getZ();
        
        boolean zSame = this.corner1.getX() == other.corner1.getX() && this.corner2.getX() == other.corner2.getX() &&
                        this.corner1.getY() == other.corner1.getY() && this.corner2.getY() == other.corner2.getY();
        
        return xSame || ySame || zSame;
    };

    /** Merge Regions */
    public Region merge(Region other){
        Vector3i newCorner1 = new Vector3i(
            Math.min(this.corner1.getX(), other.corner1.getX()),
            Math.min(this.corner1.getY(), other.corner1.getY()),
            Math.min(this.corner1.getZ(), other.corner1.getZ())
        );
        Vector3i newCorner2 = new Vector3i(
            Math.max(this.corner2.getX(), other.corner2.getX()),
            Math.max(this.corner2.getY(), other.corner2.getY()),
            Math.max(this.corner2.getZ(), other.corner2.getZ())
        );
        Region newRegion = new Region(newCorner1, newCorner2);
        newRegion.adjacentRegions.addAll(this.adjacentRegions);
        newRegion.adjacentRegions.addAll(other.adjacentRegions);
        return newRegion;
    };

    /** Calculates the volume of the region */
    public long getVolume(){
        long length = corner2.getX() - corner1.getX() + 1;
        long width = corner2.getY() - corner1.getY() + 1;
        long height = corner2.getZ() - corner1.getZ() + 1;
        return length * width * height;
    };

    /** Add adjacenct Region */
    public void addAdjacentRegion(Region region) {
        this.adjacentRegions.add(region);
        region.adjacentRegions.add(this);
    }

    /** Getters for corners */
    public Vector3i getCorner1() {
        return corner1;
    }

    public Vector3i getCorner2() {
        return corner2;
    }
}