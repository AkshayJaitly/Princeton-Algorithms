/* *****************************************************************************
 *  Name: Akshay Jaitly
 *  Date: 08/23/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int size;

    private static class Node {
        private final Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree

        public Node(Point2D p) {
            this.p = p;
        }
    }

    // construct an empty set of points
    public KdTree() {
        root = null;
        size = 0;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        // corner cases
        if (p == null) throw new IllegalArgumentException("Calls insert() with a null point");

        Node inputNode = new Node(p);
        double xmin = 0, ymin = 0, xmax = 1, ymax = 1;

        root = insert(root, inputNode, true, xmin, ymin, xmax, ymax);
    }

    private Node insert(Node n, Node inputNode, boolean isEven, double xmin, double ymin,
                        double xmax, double ymax) {
        if (n == null) {
            size++;
            inputNode.rect = new RectHV(xmin, ymin, xmax, ymax);
            return inputNode;
        }
        else if (n.p.equals(inputNode.p)) return n;

        // if the point to be inserted has a smaller y-coordinate than the point in the
        // node, go left;
        // otherwise go right
        if (isEven) {
            // if the point to be inserted has a smaller x-coordinate than the point at the
            // root, go left;
            // otherwise go right
            if (inputNode.p.x() < n.p.x())
                n.lb = insert(n.lb, inputNode, !isEven, xmin, ymin, n.p.x(), ymax);
            else n.rt = insert(n.rt, inputNode, !isEven, n.p.x(), ymin, xmax, ymax);
        }
        else if (inputNode.p.y() < n.p.y()) {
            n.lb = insert(n.lb, inputNode, !isEven, xmin, ymin, xmax, n.p.y());
        }
        else n.rt = insert(n.rt, inputNode, !isEven, xmin, n.p.y(), xmax, ymax);
        return n;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException("argument to contains() is null");
        return contains(root, p, true);
    }

    private boolean contains(Node n, Point2D p, boolean isEven) {
        if (n == null) return false;
        else if (n.p.equals(p)) return true;

        if (isEven) {
            // if the point has a smaller x-coordinate than the point at the root, go left;
            // otherwise go right
            return p.x() < n.p.x() ? contains(n.lb, p, false) : contains(n.rt, p, false);
        }
        else {
            // if the point has a smaller y-coordinate than the point in the node, go left;
            // otherwise go right
            return p.y() < n.p.y() ? contains(n.lb, p, true) : contains(n.rt, p, true);
        }
    }

    // draw all points to standard draw
    public void draw() {
        if (root == null) return;
        drawHelper(root, true);
    }

    private void drawHelper(Node n, boolean isEven) {
        // first draw the node
        drawNode(n);

        // second draw the splitting lines
        drawSplittingLine(n, isEven);

        // third recursive draw the child nodes
        if (n.lb != null) drawHelper(n.lb, !isEven);
        if (n.rt != null) drawHelper(n.rt, !isEven);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        // corner cases
        if (rect == null) throw new IllegalArgumentException("Calls range() with a null point");
        Queue<Point2D> res = new Queue<Point2D>();
        if (root == null) return res;
        rangeAssist(rect, root, res);
        return res;
    }

    private void rangeAssist(RectHV rect, Node n, Queue<Point2D> res) {
        if (n == null) return;
        if (!rect.intersects(n.rect)) return;
        if (rect.contains(n.p)) res.enqueue(n.p);
        rangeAssist(rect, n.lb, res);
        rangeAssist(rect, n.rt, res);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        // corner cases
        if (p == null) throw new IllegalArgumentException("Calls nearest() with a null point");
        if (root == null) return null;

        Point2D min = root.p;
        return nearestHelper(root, p, min);
    }

    private Point2D nearestHelper(Node n, Point2D p, Point2D min) {
        if (n == null) return min;
        // if the closest point discovered so far is closer than
        // the distance between the query point and the rectangle corresponding to a
        // node,
        // there is no need to explore that node (or its subtrees).
        if (p.distanceSquaredTo(min) < n.rect.distanceSquaredTo(p)) return min;

        // update min based on current node
        if (n.p.distanceSquaredTo(p) < min.distanceSquaredTo(p)) min = n.p;

        // The effectiveness of the pruning rule depends on quickly finding a nearby
        // point.
        // To do this, organize the recursive method so that when there are two possible
        // subtrees to go down,
        // you always choose the subtree that is on the same side of the splitting line
        // as the query point
        // as the first subtree to explore the closest point found while exploring the
        // first subtree may enable pruning of the second subtree.
        if (n.lb == null) return nearestHelper(n.rt, p, min);
        else if (n.rt == null) return nearestHelper(n.lb, p, min);
        else if (n.lb.rect.distanceSquaredTo(p) < n.rt.rect.distanceSquaredTo(p)) {
            min = nearestHelper(n.lb, p, min);
            return nearestHelper(n.rt, p, min);
        }
        else {
            min = nearestHelper(n.rt, p, min);
            return nearestHelper(n.lb, p, min);
        }
    }

    private void drawSplittingLine(Node n, boolean isEven) {
        StdDraw.setPenRadius();
        if (isEven) {
            StdDraw.setPenColor(StdDraw.RED);
            Point2D up = new Point2D(n.p.x(), n.rect.ymax());
            Point2D down = new Point2D(n.p.x(), n.rect.ymin());
            // draw splitting line
            up.drawTo(down);
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            Point2D left = new Point2D(n.rect.xmin(), n.p.y());
            Point2D right = new Point2D(n.rect.xmax(), n.p.y());
            left.drawTo(right);
        }
    }


    private void drawNode(Node n) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        n.p.draw();
    }

    public static void main(String[] args) {
        KdTree tree = new KdTree();
        Point2D p1 = new Point2D(0.7, 0.2);
        System.out.println("tree initialization size: " + tree.size());
        tree.insert(p1);
        System.out.println("tree p1 size: " + tree.size());
        System.out.println("tree contains p1: " + tree.contains(p1));
        System.out.println("tree contains new p1: " + tree.contains(new Point2D(0, 0.1)));
        tree.insert(p1);
        System.out.println("tree p1 size: " + tree.size());
        Point2D p2 = new Point2D(0.5, 0.4);
        tree.insert(p2);
        Point2D p3 = new Point2D(0.2, 0.3);
        tree.insert(p3);
        tree.insert(new Point2D(0.4, 0.7));
        tree.insert(new Point2D(0.9, 0.6));
        tree.draw();
    }
}