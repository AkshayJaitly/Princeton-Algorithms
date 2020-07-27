/* *****************************************************************************
 *  Name:              Akshay Jaitly
 *  Last modified:     07/26/2020
 **************************************************************************** */


import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private final WeightedQuickUnionUF grid;
    private final WeightedQuickUnionUF full;
    private final int n;
    private final int top;
    private final int bottom;
    private int numberOfOpenSites;
    private boolean[] openNodes;


    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("N has to be greater than 0.Please check");
        }
        grid = new WeightedQuickUnionUF(n * n + 2);
        full = new WeightedQuickUnionUF(n * n + 1);

        this.n = n;
        top = getArrayIndex(n, n) + 1;
        bottom = getArrayIndex(n, n) + 2;
        numberOfOpenSites = 0;
        openNodes = new boolean[n * n];
    }


    // Check valid condition
    private boolean isValid(int row, int col) {
        return row > 0 && col > 0 && row <= n && col <= n;
    }

    private void outOfBoundsCheck(int row, int col) {
        if (!isValid(row, col)) {
            throw new IllegalArgumentException(" Values are out of bounds.Please check");
        }
    }

    private int getArrayIndex(int row, int col) {
        outOfBoundsCheck(row, col);
        return (n * (row - 1) + col) - 1;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        outOfBoundsCheck(row, col);

        if (isOpen(row, col)) return;

        numberOfOpenSites++;
        int index = getArrayIndex(row, col);
        openNodes[index] = true;

        if (row == 1) {
            grid.union(top, index);
            full.union(top, index);
        }

        if (row == n) {
            grid.union(bottom, index);
        }

        if (isValid(row, col + 1) && isOpen(row, col + 1)) {
            grid.union(getArrayIndex(row, col + 1), index);
            full.union(getArrayIndex(row, col + 1), index);
        }

        if (isValid(row, col - 1) && isOpen(row, col - 1)) {
            grid.union(getArrayIndex(row, col - 1), index);
            full.union(getArrayIndex(row, col - 1), index);
        }

        if (isValid(row - 1, col) && isOpen(row - 1, col)) {
            grid.union(getArrayIndex(row - 1, col), index);
            full.union(getArrayIndex(row - 1, col), index);
        }



        if (isValid(row + 1, col) && isOpen(row + 1, col)) {
            grid.union(getArrayIndex(row + 1, col), index);
            full.union(getArrayIndex(row + 1, col), index);

        }

    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        outOfBoundsCheck(row, col);
        return openNodes[getArrayIndex(row, col)];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        int idx = getArrayIndex(row, col);
        return full.find(idx) == full.find(top);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return numberOfOpenSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return grid.find(top) == grid.find(bottom);
    }

    // test client (optional)
    public static void main(String[] args) {
        System.out.println("Percolation started");
    }
}
