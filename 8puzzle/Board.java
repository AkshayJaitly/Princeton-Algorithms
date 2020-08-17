/* *****************************************************************************
 *  name: Akshay Jaitly
 *  Date: 08/16
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Board {
    private int[][] tiles;
    private int n;
    // record blank position
    private int blankRow;
    private int blankCol;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        if (tiles == null) n = 0;
        else n = tiles[0].length;
        this.tiles = new int[n][n];
        int i = 0;
        while (i < n) {
            int j = 0;
            while (j < n) {
                this.tiles[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    blankRow = i;
                    blankCol = j;
                }
                j++;
            }
            i++;
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(n + "\n");
        int i = 0;
        while (i < n) {
            int j = 0;
            while (j < n) {
                s.append(String.format("%2d ", tiles[i][j]));
                j++;
            }
            s.append("\n");
            i++;
        }
        return s.toString();
    }

    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int count = 0;
        int i = 0;
        while (i < n) {
            int j = 0;
            while (j < n) {
                int num = tiles[i][j];
                if (num != 0) {
                    if (i == n - 1 && j == n - 1) {
                        int ref = 0;
                        if (num != ref)
                            count++;
                    }
                    else {
                        int ref = n * i + j + 1;
                        if (num != ref)
                            count++;
                    }
                }
                j++;
            }
            i++;
        }
        return count;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int distance = 0;
        int i = 0;
        while (i < n) {
            int j = 0;
            while (j < n) {
                int num = tiles[i][j];
                if (num != 0) {
                    int refI = (num - 1) / n;
                    int refJ = (num - 1) % n;
                    distance = distance + Math.abs(i - refI) + Math.abs(j - refJ);
                }
                j++;
            }
            i++;
        }
        return distance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return manhattan() == 0;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        // Copy current Board
        Board twin = new Board(tiles);
        // exchange
        if ((blankRow == 0 && blankCol == 0) || (blankRow == 0 && blankCol == 1)) {
            int temp = twin.tiles[1][0];
            twin.tiles[1][0] = twin.tiles[1][1];
            twin.tiles[1][1] = temp;
        }
        else {
            int temp = twin.tiles[0][0];
            twin.tiles[0][0] = twin.tiles[0][1];
            twin.tiles[0][1] = temp;
        }
        return twin;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() == this.getClass()) {
            Board that = (Board) y;
            return Arrays.deepEquals(this.tiles, that.tiles);
        }
        else {
            return false;
        }
    }


    // all neighboring boards
    public Iterable<Board> neighbors() {
        int i = 0;
        int j = 0;
        boolean flag = false;
        for (i = 0; i < n; i++) {
            for (j = 0; j < n; j++) {
                if (tiles[i][j] == 0) {
                    flag = true;
                    break;
                }
            }
            if (flag) {
                break;
            }
        }

        int up = i - 1, down = i + 1, left = j - 1, right = j + 1;

        List<Board> list = new ArrayList<Board>();
        if (indexValid(up) && indexValid(j)) {
            list.add(getSwapBoard(up, j, i, j));
        }
        if (indexValid(down) && indexValid(j)) {
            list.add(getSwapBoard(down, j, i, j));
        }
        if (indexValid(i) && indexValid(left)) {
            list.add(getSwapBoard(i, left, i, j));
        }
        if (indexValid(i) && indexValid(right)) {
            list.add(getSwapBoard(i, right, i, j));
        }
        return list;
    }


    private Board getSwapBoard(int firstX, int firstY, int secondX, int secondY) {
        if (!indexValid(firstX) || !indexValid(firstY) || !indexValid(secondX) || !indexValid(
                secondY)) return null;

        int[][] tempBoard = new int[n][n];
        for (int i = 0; i < n; i++) System.arraycopy(tiles[i], 0, tempBoard[i], 0, n);
        int temp = tempBoard[firstX][firstY];
        tempBoard[firstX][firstY] = tempBoard[secondX][secondY];
        tempBoard[secondX][secondY] = temp;
        return new Board(tempBoard);
    }


    private boolean indexValid(int x) {
        return x >= 0 && x < n;
    }


    public static void main(String[] args) {
        int[][] initial = {
                { 0, 1, 2 },
                { 3, 4, 5 },
                { 6, 7, 8 }
        };
        Board board = new Board(initial);

        System.out.println(board.toString());

        int[][] ints = {
                { 0, 1, 2, 3 },
                { 3, 4, 5, 5 },
                { 6, 7, 8, 8 },
                { 9, 1, 2, 2 }
        };
        Board fourBoard = new Board(ints);

        System.out.println("Equals for 2 boards: " + board.equals(fourBoard));
        System.out.println("board dimension: " + board.dimension());
        System.out.println("fourBoard dimension: " + fourBoard.dimension());

        int[][] hamming = {
                { 8, 1, 3 },
                { 4, 0, 2 },
                { 7, 6, 5 }
        };
        Board hammingBoard = new Board(hamming);
        System.out.println("hamming: " + hammingBoard.hamming());
        System.out.println("manhattan: " + hammingBoard.manhattan());
        System.out.println("Is goal board: " + hammingBoard.isGoal());

        int[][] goal = {
                { 1, 2, 3 },
                { 4, 5, 6 },
                { 7, 8, 0 }
        };
        Board goalBoard = new Board(goal);
        System.out.println("Should be goal board: " + goalBoard.isGoal());
        System.out.println("hamming: " + goalBoard.hamming());
        System.out.println("manhattan: " + goalBoard.manhattan());

        System.out.println("A twin board of: ");
        System.out.println(hammingBoard.toString());
        System.out.println("can be: ");
        System.out.println(hammingBoard.twin().toString());
        System.out.println("or: ");
        System.out.println(hammingBoard.twin().toString());

        int[][] neighborsArr = {
                { 8, 1, 3 },
                { 4, 2, 0 },
                { 7, 6, 5 }
        };
        Board neighborsBoard = new Board(neighborsArr);
        for (Board b : neighborsBoard.neighbors()) {
            System.out.println("neighbor board:");
            System.out.println(b.toString());
        }
    }
}
