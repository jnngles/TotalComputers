package com.jnngl.totalcomputers;

import org.bukkit.util.Vector;

/**
 * Some methods from AABB created by NathanWolf
 * @author NathanWolf
 */
public class AABB {

    private final Vector min, max;

    /**
     * Constructor
     * @param min Minimum position
     * @param max Maximum position
     */
    public AABB(Vector min, Vector max) {
        this.min = new Vector(Math.min(min.getX(), max.getX()), Math.min(min.getY(), max.getY()), Math.min(min.getZ(), max.getZ()));
        this.max = new Vector(Math.max(min.getX(), max.getX()), Math.max(min.getY(), max.getY()), Math.max(min.getZ(), max.getZ()));
    }

    /**
     * Constructor
     * @param center Center of the AABB
     * @param dMinX X offset
     * @param dMaxX X offset
     * @param dMinY Y offset
     * @param dMaxY Y offset
     * @param dMinZ Z offset
     * @param dMaxZ Z offset
     */
    public AABB(Vector center, double dMinX, double dMaxX, double dMinY, double dMaxY, double dMinZ, double dMaxZ)
    {
        this.min = new Vector(center.getX() + dMinX, center.getY() + dMinY, center.getZ() + dMinZ);
        this.max = new Vector(center.getX() + dMaxX, center.getY() + dMaxY, center.getZ() + dMaxZ);
    }

    /**
     * Dependency of {@link #getIntersection(double, double, Vector, Vector, int)}
     * @param hit Idk
     * @param axis Idk
     * @return Idk
     */
    protected boolean inBox(Vector hit, int axis) {
        if (axis==1 && hit.getZ() > min.getZ() && hit.getZ() < max.getZ() && hit.getY() > min.getY() && hit.getY() < max.getY()) return true;
        if (axis==2 && hit.getZ() > min.getZ() && hit.getZ() < max.getZ() && hit.getX() > min.getX() && hit.getX() < max.getX()) return true;
        return axis == 3 && hit.getX() > min.getX() && hit.getX() < max.getX() && hit.getY() > min.getY() && hit.getY() < max.getY();
    }

    /**
     * Dependency of {@link #getIntersection(Vector, Vector)}
     * @param fDst1 Idk
     * @param fDst2 Idk
     * @param P1 Idk
     * @param P2 Idk
     * @param side Idk
     * @return Intersection
     */
    protected Vector getIntersection(double fDst1, double fDst2, Vector P1, Vector P2, int side) {
        if ((fDst1 * fDst2) >= 0.0f) return null;
        if (fDst1 == fDst2) return null;
        Vector P2_clone = P2.clone();
        P2_clone = P1.clone().add(P2_clone.subtract(P1).multiply(-fDst1 / (fDst2 - fDst1)));
        return inBox(P2_clone, side) ? P2_clone : null;
    }

    /**
     * @param p1 Point 1
     * @param p2 Point 2
     * @return Exact position of line intersection with AABB
     */
    public Vector getIntersection(Vector p1, Vector p2)
    {
        Vector currentHit;
        Vector hit = getIntersection(p1.getX() - min.getX(), p2.getX() - min.getX(), p1, p2, 1);
        currentHit = hit;

        hit = getIntersection(p1.getY() - min.getY(), p2.getY() - min.getY(), p1, p2, 2);
        if (currentHit != null && hit != null) {
            if (currentHit.distanceSquared(p1) < hit.distanceSquared(p1)) return currentHit; else return hit;
        } else if (currentHit == null) {
            currentHit = hit;
        }

        hit = getIntersection(p1.getZ() - min.getZ(), p2.getZ() - min.getZ(), p1, p2, 3);
        if (currentHit != null && hit != null) {
            if (currentHit.distanceSquared(p1) < hit.distanceSquared(p1)) return currentHit; else return hit;
        } else if (currentHit == null) {
            currentHit = hit;
        }

        hit = getIntersection(p1.getX() - max.getX(), p2.getX() - max.getX(), p1, p2, 1);
        if (currentHit != null && hit != null) {
            if (currentHit.distanceSquared(p1) < hit.distanceSquared(p1)) return currentHit; else return hit;
        } else if (currentHit == null) {
            currentHit = hit;
        }

        hit = getIntersection(p1.getY() - max.getY(), p2.getY() - max.getY(), p1, p2, 2);
        if (currentHit != null && hit != null) {
            if (currentHit.distanceSquared(p1) < hit.distanceSquared(p1)) return currentHit; else return hit;
        } else if (currentHit == null) {
            currentHit = hit;
        }

        hit = getIntersection(p1.getZ() - max.getZ(), p2.getZ() - max.getZ(), p1, p2, 3);
        if (currentHit != null && hit != null) {
            if (currentHit.distanceSquared(p1) < hit.distanceSquared(p1)) return currentHit; else return hit;
        } else if (hit != null) {
            return hit;
        }
        return currentHit;
    }

}
