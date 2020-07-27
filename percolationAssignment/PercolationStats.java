/* *****************************************************************************
 *  Name:              Akshay Jaitly
 *  Last modified:     07/26/2020
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private final int n;
    private final int t;
    private final double[] fractions;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {

        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException(
                    "n & trials have to be greater than zero!");
        }

        this.n = n;
        this.t = trials;
        fractions = new double[trials];

        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);

            int count = 0;

            while (!p.percolates()) {
                openRandomNode(p);
                count++;
            }

            fractions[i] = (double) count / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(fractions);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(fractions);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + ((CONFIDENCE_95 * stddev()) / Math.sqrt(t));
    }

    private void openRandomNode(Percolation p) {
        boolean isNodeopen = true;
        int row = 0, col = 0;

        while (isNodeopen) {
            row = StdRandom.uniform(1, n + 1);
            col = StdRandom.uniform(1, n + 1);

            isNodeopen = p.isOpen(row, col);
        }
        p.open(row, col);
    }

    // test client
    public static void main(String[] args) {
        if (args.length != 2) throw new IllegalArgumentException("Check the args! ");
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);

        PercolationStats stats = new PercolationStats(n, trials);

        StdOut.printf("mean                    = %f%n", stats.mean());
        StdOut.printf("stddev                  = %f%n", stats.stddev());
        StdOut.printf("95%% confidence interval = [%f, %f]%n", stats.confidenceLo(),
                      stats.confidenceHi());
    }
}