/* *****************************************************************************
 *  Name: Akshay Jaitly
 *  Date: 08/02/2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int val;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[1];
        val = 0;
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return val == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return val;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) throw new IllegalArgumentException("You can not add a null item");
        if (val == items.length) resize(2 * items.length);
        items[val++] = item;
    }


    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < val; i++) {
            temp[i] = items[i];
        }
        items = temp;
    }

    private void swapItem(int source, int dest) {
        items[source] = items[dest];
        items[dest] = null;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) throw new NoSuchElementException();
        int random = StdRandom.uniform(val);
        int lastIndex = val - 1;
        Item item = items[random];
        items[random] = null;
        if (random != lastIndex) swapItem(random, lastIndex);
        if (val > 0 && val == items.length / 4) resize(items.length / 2);
        val--;
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        return items[StdRandom.uniform(val)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new ArrayIterator();
    }

    private class ArrayIterator implements Iterator<Item> {

        Item[] temp;
        int curr = 0;

        public ArrayIterator() {
            temp = (Item[]) new Object[val];
            for (int i = 0; i < val; i++) {
                temp[i] = items[i];
            }
            StdRandom.shuffle(temp);

        }

        @Override
        public boolean hasNext() {
            return curr < val;
        }

        @Override
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return temp[curr++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }

    }
    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 16; i++) {
            randomizedQueue.enqueue(i);
        }
        System.out.println(randomizedQueue.size());
        for (Integer i : randomizedQueue) {
            System.out.println(i);
        }
        System.out.println("sample:" + randomizedQueue.sample());
        System.out.println("dequeue");
        while (!randomizedQueue.isEmpty()) System.out.println(randomizedQueue.dequeue());
        System.out.println(randomizedQueue.size());
    }

}