import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private Node<Item> head;
    private Node<Item> tail;
    private int sizeCounter;

    // construct an empty deque
    public Deque() {

    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        return sizeCounter;
    }

    // add the item to the front
    public void addFirst(Item item) {
        notNull(item);
        Node<Item> node = new Node<>(item);
        if (isEmpty()) {
            head = node;
            tail = node;
            sizeCounter++;
            return;
        }
        node.previous = head;
        head.next = node;
        head = node;
        sizeCounter++;
    }

    // add the item to the back
    public void addLast(Item item) {
        notNull(item);
        Node<Item> newNode = new Node<>(item);
        if (isEmpty()) {
            head = newNode;
            tail = newNode;
            sizeCounter++;
            return;
        }
        newNode.next = tail;
        tail.previous = newNode;
        tail = newNode;
        sizeCounter++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        notEmpty();
        Item result = head.value;
        if (isLastElementInDeque()) {
            head = null;
            tail = null;
        } else {
            head = head.previous;
            head.next = null;
        }
        sizeCounter--;
        return result;
    }

    // remove and return the item from the back
    public Item removeLast() {
        notEmpty();
        if (isLastElementInDeque()) {
            return removeFirst();
        }
        Item result = tail.value;
        tail = tail.next;
        tail.previous = null;
        sizeCounter--;
        return result;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<>(head);
    }

    private boolean isLastElementInDeque() {
        return sizeCounter == 1;
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
        private Item value;
        private Node<Item> next;
        private Node<Item> previous;

        private Node() {
        }

        private Node(Item value) {
            this.value = value;
        }
    }

    private static class DequeIterator<Item> implements Iterator<Item> {

        private Node<Item> pointer;

        public DequeIterator(Node<Item> startNode) {
            pointer = new Node<>();
            pointer.previous = startNode;
        }

        public boolean hasNext() {
            return pointer.previous != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("Iterator has no values");
            }
            Node<Item> node = pointer.previous;
            pointer = node;
            return node.value;
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<String> stringDeque = new Deque<>();
        stringDeque.addFirst("c");
        stringDeque.addFirst("b");
        stringDeque.addFirst("a");
        for (String s : stringDeque) {
            System.out.println(s);
        }

        System.out.println(stringDeque.removeFirst());
        System.out.println(stringDeque.removeFirst());
        System.out.println(stringDeque.removeFirst());

        Deque<Integer> integerDeque = new Deque<>();
        integerDeque.addLast(1);
        integerDeque.addLast(2);
        integerDeque.addLast(3);
        for (int i : integerDeque) {
            System.out.println(i);
        }
        System.out.println(integerDeque.removeLast());
        System.out.println(integerDeque.removeFirst());
        System.out.println(integerDeque.removeLast());
    }

}