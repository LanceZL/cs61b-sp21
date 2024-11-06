package deque;

public interface Deque<T> extends Iterable<T> {

    void addFirst(T x);

    void addLast(T x);

    default boolean isEmpty() {
        return size() == 0;
    };

    int size();

    T removeFirst();

    T removeLast();

    T get(int index);

    void printDeque();
}
