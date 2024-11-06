package deque;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ArrayDeque<T> implements Iterable<T> {
    T[] array;
    // Use two pointers, head and tail to track the deque.
    // To eliminate the edge case, let the head = -1, and tail = 0.
    // They will never overlap since we will resize the Deque first.
    private int size, head, tail;
    private final int RFACTOR = 2;
    private final double THRESHOLD = 0.25;
    private final int MINIMUMSIZE = 8;
    private final int SMALLSIZE = 16;
    private final int SHRUNKSTEP = 2;

    public ArrayDeque() {
        array = (T[]) new Object[8];
        size = 0;
        head = -1;
        tail = 0;
    }

    public ArrayDeque(int capacity) {
        array = (T[]) new Object[capacity];
        size = 0;
        head = -1;
        tail = 0;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null) {
            return false;
        }

        if (!(o instanceof ArrayDeque<?>)) {
            return false;
        }

        // Compare size
        ArrayDeque<T> other = (ArrayDeque<T>) o;
        if (this.size != other.size) {
            return false;
        }
        // Compare the elements
        for (int i = 0; i < size; i++) {
            if (get(i) != other.get(i)) {
                return false;
            }
        }

        return true;
    }

    public void printDeque() {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < size; i++) {
            res.append(get(i));
            res.append(" ");
        }
        System.out.println(res);
    }

    @Override
    public String toString() {
        return toList().toString();
    }

    public void addFirst(T x) {
        if (size == array.length) {
            resizeUp();
        }

        // add the element.
        array[accessibleIndex(head)] = x;
        head -= 1;
        size += 1;
    }

    private void resizeUp() {
        resize(size * RFACTOR);
    }

    private boolean shouldResizeDown() {
        if (array.length == MINIMUMSIZE) {
            return false;
        }

        return ((double) size / array.length) <= THRESHOLD;
    }

    private int shrunkSize() {
        if (array.length <= SMALLSIZE) {
            return Math.min(array.length - SHRUNKSTEP, MINIMUMSIZE);
        }

        return array.length / RFACTOR;
    }

    private void resizeDown() {
        resize(shrunkSize());
    }

    private void resize(int newSize) {
        T[] newArray = (T[]) new Object[newSize];

        for (int i = 0; i < size; i++) {
            newArray[i] = get(i);
        }

        // reset the head and tail.
        head = -1;
        tail = size;
        array = newArray;
    }

    /**
     * Returns the real accessible head index of the Deque.
     * size = 8
     * head : 0 -> 0
     * head : -1 -> 7
     * */
    private int accessibleIndex(int i) {
        return Math.floorMod(i, array.length);
    }

    public void addLast(T x) {
        if (size == array.length) {
            resizeUp();
        }

        array[accessibleIndex(tail)] = x;
        tail += 1;
        size += 1;
    }

    public List<T> toList() {
        if (size == 0) {
            return List.of();
        }

        List<T> res = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            res.add(array[accessibleIndex(head + i + 1)]);
        }
        return res;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public T removeFirst() {
        if (size == 0) {
            return null;
        }

        if (shouldResizeDown()) {
            resizeDown();
        }

        head += 1;  // update head
        T res = array[accessibleIndex(head)];  // save result
        array[accessibleIndex(head)] = null;  // Delete reference
        size -= 1;   // update size

        return res;
    }

    public T removeLast() {
        if (size == 0) {
            return null;
        }

        if (shouldResizeDown()) {
            resizeDown();
        }

        tail -= 1;
        T res = array[accessibleIndex(tail)];
        array[accessibleIndex(tail)] = null;
        size -= 1;

        return res;
    }

    public T get(int index) {
        if (index < 0 || index >= size) {
            return null;
        }
        return array[accessibleIndex(index + head + 1)];
    }

    public T getRecursive(int index) {
        throw new UnsupportedOperationException("No need to implement getRecursive for proj 1b");
    }

    public void clear() {
        array = (T[]) new Object[8];
        size = 0;
        head = -1;
        tail = 0;
    }

    public Iterator<T> iterator() {
        return new ArrayDeque61BIterator();
    }

    private class ArrayDeque61BIterator implements Iterator<T> {
        private int pos;

        @Override
        public boolean hasNext() {
            return pos < size;
        }

        @Override
        public T next() {
            T res = get(pos);
            pos++;
            return res;
        }
    }
}
