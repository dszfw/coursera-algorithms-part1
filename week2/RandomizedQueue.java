import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Node<Item> first;
    private Node<Item> last;
    private Node<Item>[] nodes;
    private int nodesCounter;

    public RandomizedQueue() {
        nodes = (Node<Item>[]) new Node[1];
    }

    public boolean isEmpty() {
        return nodesCounter == 0;
    }

    public int size() {
        return nodesCounter;
    }

    public void enqueue(Item item) {
        notNull(item);
        Node<Item> node = new Node<>(item);
        if (isEmpty()) {
            first = node;
        }
        else {
            node.next = last;
            last.previous = node;
        }
        last = node;
        addToArray(node);
        if (nodes.length == nodesCounter) {
            resizeNodesArray();
        }
    }

    public Item dequeue() {
        notEmpty();
        Node<Item> randomNode = removeRandomNodeFromArray();
        Node<Item> prevNode = randomNode.previous;
        Node<Item> nextNode = randomNode.next;

        if (prevNode != null) {
            prevNode.next = nextNode;
        }
        if (nextNode != null) {
            nextNode.previous = prevNode;
        }
        if (randomNode == last) {
            last = randomNode.next;
        }
        if (randomNode == first) {
            first = randomNode.previous;
        }

        if (arrayLengthIsGreaterAtLeastFourTimesThanCountsOfNodes()) {
            resizeNodesArray();
        }

        return randomNode.value;
    }

    public Item sample() {
        notEmpty();
        int randomIndex = getRandomIndex();
        while (nodes[randomIndex] == null) {
            randomIndex = getRandomIndex();
        }
        return nodes[randomIndex].value;
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>(last, nodesCounter);
    }

    private void addToArray(Node<Item> node) {
        nodes[nodesCounter] = node;
        nodesCounter++;
    }

    private Node<Item> removeRandomNodeFromArray() {
        Node<Item> randomNode;
        int randomIndex = getRandomIndex();
        randomNode = nodes[randomIndex];
        nodes[randomIndex] = nodes[nodesCounter - 1];
        nodes[nodesCounter - 1] = null;
        nodesCounter--;
        return randomNode;
    }

    private void resizeNodesArray() {
        nodes = (Node<Item>[]) new Node[1 + nodesCounter * 2];
        Node<Item> node = last;
        for (int i = 0; i < nodesCounter; i++) {
            nodes[i] = node;
            node = node.next;
        }
    }

    private boolean arrayLengthIsGreaterAtLeastFourTimesThanCountsOfNodes() {
        if (nodesCounter == 0) {
            return true;
        }
        return nodes.length / nodesCounter >= 4;
    }

    private int getRandomIndex() {
        return StdRandom.uniform(nodesCounter);
    }

    private void notEmpty() {
        if (isEmpty()) throw new NoSuchElementException("Deque is empty");
    }

    private void notNull(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Illegal argument, null value");
        }
    }

    private static class Node<Item> {
        Item value;
        Node<Item> next;
        Node<Item> previous;

        public Node() {
        }

        public Node(Item value) {
            this.value = value;
        }
    }

    private static class RandomizedQueueIterator<Item> implements Iterator<Item> {

        private final Item[] items;
        private int index;

        public RandomizedQueueIterator(Node<Item> startNode, int size) {
            index = 0;
            items = (Item[]) new Object[size];
            Node<Item> node = startNode;
            for (int i = 0; i < size; i++) {
                items[i] = node.value;
                node = node.next;
            }
            StdRandom.shuffle(items);
        }

        public boolean hasNext() {
            return index < items.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator has no values");
            }
            Item item = items[index];
            index++;
            return item;
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<String> queue = new RandomizedQueue<>();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        queue.enqueue("6");
        queue.enqueue("7");
        queue.enqueue("8");
        queue.enqueue("9");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.enqueue("10");
        for (String s : queue) {
            System.out.println(s);
        }
        System.out.println("-------------");
        for (String s : queue) {
            System.out.println(s);
        }

    }
}
