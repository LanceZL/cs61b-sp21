package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LinkedListDeque<T> implements Iterable<T> {
    private final Node sentinel;
    private int size;

    public LinkedListDeque() {
        Node s = new Node(null);
        s.next = s;
        s.prev = s;
        sentinel = s;
        size = 0;
    }

    
    public Iterator<T> iterator() {
        return new LinkedListDeque61BIterator();
    }

    
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }

        if (!(o instanceof LinkedListDeque<?>)) {
            return false;
        }

        // 1. compare size
        LinkedListDeque<T> other = (LinkedListDeque<T>) o;
        if (other.size != this.size) {
            return false;
        }
        Iterator<T> iterator1 = this.iterator();
        Iterator<T> iterator2 = other.iterator();
        while (iterator1.hasNext()) {
            T item1 = iterator1.next();
            T item2 = iterator2.next();
            if (item1 != item2) {
                return false;
            }
        }

        return true;
    }

    
    public String toString() {
        return toList().toString();
    }

    public void printDeque() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(get(i));
            res.append(" ");
        }
        System.out.println(res);
    }

    /**
     * Add {@code x} to the front of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    
    public void addFirst(T x) {
        Node currFirst = sentinel.next;
        Node newFirst = new Node(x);

        // Non-empty : sen <-> 1 <-> 2 <-> sen
        // Empty : sen <-> sen
        sentinel.next = newFirst;
        newFirst.prev = sentinel;
        newFirst.next = currFirst;
        currFirst.prev = newFirst;

        // update the size.
        size += 1;
    }

    /**
     * Add {@code x} to the back of the deque. Assumes {@code x} is never null.
     *
     * @param x item to add
     */
    
    public void addLast(T x) {
        Node currLast = sentinel.prev;
        Node newLast = new Node(x);

        currLast.next = newLast;
        newLast.prev = currLast;
        newLast.next = sentinel;
        sentinel.prev = newLast;

        size += 1;
    }

    /**
     * Returns a List copy of the deque. Does not alter the deque.
     *
     * @return a new list copy of the deque.
     */
    
    public List<T> toList() {
        if (size == 0) {
            return List.of();
        }

        List<T> items = new ArrayList<>();

        // Pointer to iterate the deque.
        Node curr = sentinel.next;
        for (int i = 0; i < size; i++) {
            items.add(curr.item);
            curr = curr.next;
        }
        return items;
    }

    /**
     * Returns if the deque is empty. Does not alter the deque.
     *
     * @return {@code true} if the deque has no elements, {@code false} otherwise.
     */
    
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Returns the size of the deque. Does not alter the deque.
     *
     * @return the number of items in the deque.
     */
    
    public int size() {
        return size;
    }

    /**
     * Remove and return the element at the front of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    
    public T removeFirst() {
        if (isEmpty()) {
            return null;
        }

        Node currFirst = sentinel.next;
        Node newFirst = currFirst.next;
        newFirst.prev = sentinel;
        sentinel.next = newFirst;

        // Update the size
        size -= 1;

        return currFirst.item;
    }

    /**
     * Remove and return the element at the back of the deque, if it exists.
     *
     * @return removed element, otherwise {@code null}.
     */
    
    public T removeLast() {
        if (isEmpty()) {
            return null;
        }

        Node currLast = sentinel.prev;
        Node newLast = currLast.prev;
        newLast.next = sentinel;
        sentinel.prev = newLast;

        // Update the size.
        size -= 1;

        return currLast.item;
    }

    /**
     * The Deque61B abstract data type does not typically have a get method,
     * but we've included this extra operation to provide you with some
     * extra programming practice. Gets the element, iteratively. Returns
     * null if index is out of bounds. Does not alter the deque.
     *
     * @param index index to get
     * @return element at {@code index} in the deque
     */
    
    public T get(int index) {
        if (!isValidIndex(index)) {
            return null;
        }

        // curr will stop at the index.
        Node curr = sentinel.next;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }

        return curr.item;
    }

    private boolean isValidIndex(int index) {
        return index >= 0 && index < size;
    }

    /**
     * This method technically shouldn't be in the interface, but it's here
     * to make testing nice. Gets an element, recursively. Returns null if
     * index is out of bounds. Does not alter the deque.
     *
     * @param index index to get
     * @return element at {@code index} in the deque
     */
    
    public T getRecursive(int index) {
        if (!isValidIndex(index)) {
            return null;
        }

        return recursiveGet(sentinel.next, index).item;
    }

    /**
     * Helper method for getRecursive()
     */
    private Node recursiveGet(Node curr, int index) {
        if (index == 0) {
            return curr;
        }

        return recursiveGet(curr.next, index - 1);
    }

    /**
     * Non-static Node class. Accept outer class's T as it's item's type.
     */
    private class Node {
        private Node prev;
        private Node next;
        private final T item;

        public Node(T x) {
            item = x;
        }
    }

    private class LinkedListDeque61BIterator implements Iterator<T> {
        Node curr;

        public LinkedListDeque61BIterator() {
            curr = sentinel;
        }

        
        public boolean hasNext() {
            return curr.next != sentinel;
        }

        
        public T next() {
            T res = curr.next.item;
            curr = curr.next;
            return res;
        }
    }
}
