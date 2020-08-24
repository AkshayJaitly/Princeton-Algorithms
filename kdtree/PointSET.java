/* *****************************************************************************
 *  Name: Akshay Jaitly
 *  Date: 08/23/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> pointSet;

    // construct an empty set of points
    public PointSET() {
        pointSet = new SET<Point2D>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return pointSet.isEmpty();
    }

    // number of points in the set
    public int size() {
        return pointSet.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // corner cases
        if (p == null) throw new IllegalArgumentException("Input is null.");

        if (!pointSet.contains(p)) pointSet.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        // corner cases
        if (p == null) throw new IllegalArgumentException("Input is null.");

        return pointSet.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : pointSet) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // corner cases
        if (rect == null) throw new IllegalArgumentException("Input is null.");

        SET<Point2D> res = new SET<Point2D>();
        for (Point2D p : pointSet) {
            if (rect.contains(p)) res.add(p);
        }
        return res;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // corner cases
        if (p == null) throw new IllegalArgumentException("Input is null.");

        double minDistance = 2;
        Point2D res = null;

        for (Point2D that : pointSet) {
            if (!p.equals(that)) {
                double distance = p.distanceSquaredTo(that);
                if (minDistance > distance) {
                    minDistance = distance;
                    res = that;
                }
            }
            else return that;
        }
        return res;
    }

}