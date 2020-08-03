/* *****************************************************************************
 *  Name: Akshay Jaitly
 *  Date: 08/02/2020
 *  Description: Deque class
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node first;
    private Node last;
    private int val;

    private class Node {
        private final Item item;
        private Node next;
        private Node prev;

        public Node(Item item) {
            this.item = item;
            next = null;
            prev = null;
        }
    }

    // construct an empty deque
    public Deque() {
        first = null;
        last = null;
        val = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return val == 0;
    }

    // return the number of items on the deque
    public int size() {
        return val;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) throw new IllegalArgumentException("Make sure input is not null");
        Node newFirst = new Node(item);
        if (isEmpty()) {
            first = newFirst;
            last = newFirst;
        }
        else {
            newFirst.next = first;
            first.prev = newFirst;
            first = newFirst;
        }
        val++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) throw new IllegalArgumentException("Make sure input is not null");
        Node newLast = new Node(item);
        if (isEmpty()) {
            first = newLast;
            last = newLast;
        }
        else {
            newLast.prev = last;
            last.next = newLast;
            last = newLast;
        }
        val++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Node oldFirst = first;
        first = first.next;
        if (first != null) {
            first.prev = null;
        }
        if (val == 1) last = null;
        val--;
        return oldFirst.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        Node oldLast = last;
        last = last.prev;
        if (last != null) {
            last.next = null;
        }
        if (val == 1) first = null;
        val--;
        return oldLast.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new ListIterator();
    }

    private class ListIterator implements Iterator<Item> {

        private Node current = first;

        @Override
        public boolean hasNext() {
            return current != null;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }


    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<Integer>();
        deque.addFirst(1);
        deque.removeLast();
        deque.addFirst(2);
        deque.removeLast();
        deque.addLast(2);
        deque.removeLast();
        System.out.println("The size is" + deque.size());
        System.out.println(deque.isEmpty());
    }
}
