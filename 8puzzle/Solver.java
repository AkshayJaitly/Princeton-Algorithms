/* *****************************************************************************
 *  Name: Akshay Jaitly
 *  Date: 08/16
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {

    private int moves = -1;
    private Stack<Board> initialSolve;

    public Solver(Board init) {
        if (init == null) throw new IllegalArgumentException();
        SearchNode initial = new SearchNode(init, null, 0);
        // build twin board and SearchNode
        Board twinBoard = init.twin();
        SearchNode twin = new SearchNode(twinBoard, null, 0);
        findSolution(initial, twin);
    }

    // define a search node nested class
    private class SearchNode {
        private final Board board;
        // record previous board
        private final SearchNode prev;

        // how many moves to reach this search node
        private final int moves;
        private final int priority;

        private SearchNode(Board board, SearchNode prev, int moves) {
            this.board = board;
            this.prev = prev;
            this.moves = moves;
            priority = this.board.manhattan() + moves;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    private void findSolution(SearchNode initial, SearchNode twin) {

        initialSolve = new Stack<Board>();

        // build two priority queue
        MinPQ<SearchNode> initialPQ = new MinPQ<SearchNode>(new ByManhattan());
        MinPQ<SearchNode> twinPQ = new MinPQ<SearchNode>(new ByManhattan());

        // add initial and twin board
        initialPQ.insert(initial);
        twinPQ.insert(twin);

        // A* algorithm for both board
        while (true) {
            if (!initialPQ.isEmpty()) {
                SearchNode searchNode = initialPQ.delMin();
                if (searchNode.board.isGoal()) {
                    this.moves = searchNode.moves;
                    // push this sequence of answer into stack
                    while (searchNode != null) {
                        initialSolve.push(searchNode.board);
                        searchNode = searchNode.prev;
                    }
                    return;
                }
                else {
                    // find neighbors
                    Iterable<Board> neighbors = searchNode.board.neighbors();
                    for (Board b : neighbors) {
                        if (searchNode.prev != null && b.equals(searchNode.prev.board)) {
                            continue;
                        }
                        initialPQ.insert(new SearchNode(b, searchNode, searchNode.moves + 1));
                    }
                }
            }
            if (!twinPQ.isEmpty()) {
                SearchNode twinNode = twinPQ.delMin();
                if (twinNode.board.isGoal()) {
                    this.moves = -1;
                    initialSolve = null;
                    return;
                }
                else {
                    Iterable<Board> neighbors = twinNode.board.neighbors();
                    for (Board b : neighbors) {
                        if (twinNode.prev != null && b.equals(twinNode.prev.board)) {
                            continue;
                        }
                        twinPQ.insert(new SearchNode(b, twinNode, twinNode.moves + 1));
                    }
                }
            }
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        return this.moves != -1;
    }

    public int moves() {
        return this.moves;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public Iterable<Board> solution() {
        return isSolvable() ? initialSolve : null;
    }

    // Comparator
    private class ByManhattan implements Comparator<SearchNode> {
        public int compare(SearchNode b1, SearchNode b2) {
            int b1Manhattan = b1.priority;
            int b2Manhattan = b2.priority;
            return Integer.compare(b1Manhattan, b2Manhattan);
        }
    }


    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution()) StdOut.println(board);
        }
    }
}