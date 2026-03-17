package org.schlunzis.neuroevolution.sdk.util;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to represent a boundary on the track. Boundaries can be deadly walls,
 * that restrict the area of the track. But they can also be used as
 * checkpoints.
 * 
 * @author JayPi4c
 */
public class Boundary {

    private PVector a;
    private PVector b;

    /**
     * Creates a new Boundary as a line from (x1, y1) to (x2, y2).
     * 
     * @param x1 first point x
     * @param y1 first point y
     * @param x2 second point x
     * @param y2 second point y
     */
    public Boundary(double x1, double y1, double x2, double y2) {
        a = new PVector(x1, y1);
        b = new PVector(x2, y2);
    }

    /**
     * Creates a new Boundary as a line from a to b.
     * 
     * @param a first point
     * @param b second point
     */
    public Boundary(PVector a, PVector b) {
        this.a = a;
        this.b = b;
    }

    /**
     * Calculats the middle point of the boundary.
     * 
     * @return the middle point
     */
    public PVector midPoint() {
        return new PVector((a.x + b.x) * 0.5, (a.y + b.y) * 0.5);
    }

    /**
     * Creates a list of Boundaries from a list of points. The points in the list
     * will be sequentially connected, so the order of the points is important. If
     * the closed flag is set to true, the last point will be connected to the first
     * point.
     * 
     * @param points the list of points
     * @param closed whether the list is closed
     * @return the list of connected Boundaries
     */
    public static List<Boundary> createBoundaries(List<PVector> points, boolean closed) {
        List<Boundary> boundaries = new ArrayList<>();

        for (int i = 0; i < points.size() - (closed ? 0 : 1); i++) {
            PVector p1 = points.get(i);
            PVector p2 = points.get((i + 1) % points.size());
            boundaries.add(new Boundary(p1.x, p1.y, p2.x, p2.y));
        }
        return boundaries;
    }

    /**
     * Getter for the first point.
     * 
     * @return the first point
     */
    public PVector getA() {
        return a;
    }

    /**
     * Getter for the second point.
     * 
     * @return the second point
     */
    public PVector getB() {
        return b;
    }

}