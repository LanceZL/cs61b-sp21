package deque;

import java.util.Iterator;

public class LinkedListDeque<T> implements Deque<T>, Iterable<T> {
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

    @Override
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
            if (!item1.equals(item2)) {
                return false;
            }
        }


        return true;
    }

    public T getRecursive(int index) {
        if (!isValidIndex(index)) {
            return null;
        }

        return recursiveGet(sentinel.next, index).item;
    }

    @Override
    public void printDeque() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size(); i++) {
            res.append(get(i));
            res.append(" ");
        }
        System.out.println(res);
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

    @Override
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

    @Override
    public void addLast(T x) {
        Node currLast = sentinel.prev;
        Node newLast = new Node(x);

        currLast.next = newLast;
        newLast.prev = currLast;
        newLast.next = sentinel;
        sentinel.prev = newLast;

        size += 1;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
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


    @Override
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

    @Override
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

    private class Node {
        private Node prev;
        private Node next;
        private final T item;

        Node(T x) {
            item = x;
        }
    }

    private class LinkedListDeque61BIterator implements Iterator<T> {
        Node curr;

        LinkedListDeque61BIterator() {
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
