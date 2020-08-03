/* *****************************************************************************
 *  Name: Akshay Jaitly
 *  Date: 08/02/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    public static void main(String[] args) {
            int n = Integer.parseInt(args[0]);
            if (n == 0) return;

            RandomizedQueue<String> q = new RandomizedQueue<>();
            while (!StdIn.isEmpty()) {
                String item = StdIn.readString();
                q.enqueue(item);
            }

            for (int i = 0; i < n; i++) {
                StdOut.println(q.dequeue());
            }
    }
}
