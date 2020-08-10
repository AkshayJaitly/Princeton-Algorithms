/* *****************************************************************************
 *  Name:Akshay Jaitly
 *  Date: 08/09/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private final LineSegment[] lineSegments;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        // verify input
        // 1. null check
        if (points == null)
            throw new IllegalArgumentException("Input value is null.");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("Input value contains null.");
        }

        // copy input parameter to avoid direct modify
        Point[] localPoints = points.clone();

        // sort local points to avoid mutate input
        Arrays.sort(localPoints);

        // 2. duplicate check
        if (localPoints.length > 1) {
            for (int m = 1; m < localPoints.length; m++) {
                if (localPoints[m].compareTo(localPoints[m - 1]) == 0)
                    throw new IllegalArgumentException("Input value contains duplicate.");
            }
        }

        ArrayList<LineSegment> res = new ArrayList<>();

        if (localPoints.length > 3) {
            Point[] temp = localPoints.clone();
            // loop through points in backup array, and sort the temp points array
            for (Point p : localPoints) {
                Arrays.sort(temp, p.slopeOrder());
                findSegments(temp, p, res);
            }
        }

        lineSegments = res.toArray(new LineSegment[res.size()]);
    }

    // the number of line segments
    public int numberOfSegments() {
        return lineSegments.length;
    }

    // the line segments
    public LineSegment[] segments() {
        return lineSegments.clone();
    }

    // Helper Methods
    private void findSegments(Point[] points, Point p, ArrayList<LineSegment> res) {
        // start from position 1, since position 0 will be the point p itself
        int start = 1;
        double slop = p.slopeTo(points[1]);

        for (int i = 2; i < points.length; i++) {
            double tempSlop = p.slopeTo(points[i]);
            if (!collinearSlop(tempSlop, slop)) {
                // check to see whether there have already 3 equal points
                if (i - start >= 3) {
                    Point[] ls = genSegment(points, p, start, i);
                    /**
                     * Important Point: only add line segment which starts form point p to avoid
                     * duplicate
                     */
                    if (ls[0].compareTo(p) == 0) {
                        res.add(new LineSegment(ls[0], ls[1]));
                    }
                }
                // update
                start = i;
                slop = tempSlop;
            }
        }
        // situation when the last several points in the array are collinear
        if (points.length - start >= 3) {
            Point[] lastPoints = genSegment(points, p, start, points.length);
            if (lastPoints[0].compareTo(p) == 0) {
                res.add(new LineSegment(lastPoints[0], lastPoints[1]));
            }
        }
    }

    private boolean collinearSlop(double tempSlop, double slop) {
        if (Double.compare(slop, tempSlop) == 0) {
            return true;
        }
        return false;
    }

    private Point[] genSegment(Point[] points, Point p, int start, int end) {
        ArrayList<Point> temp = new ArrayList<>();
        temp.add(p);
        for (int i = start; i < end; i++) {
            temp.add(points[i]);
        }
        temp.sort(null);
        return new Point[] { temp.get(0), temp.get(temp.size() - 1) };
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints fastCollinearPoints = new FastCollinearPoints(points);
        for (LineSegment segment : fastCollinearPoints.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
