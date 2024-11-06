package deque;

import java.util.Comparator;

public class MaxArrayDeque<T> extends ArrayDeque<T> {
    private final Comparator<T> comparator;

    public MaxArrayDeque(Comparator<T> c) {
        this.comparator = c;
    }

    public T max() {
        return max(this.comparator);
    }

    public T max(Comparator<T> c) {
        if (this.isEmpty()) {
            return null;
        }

        T currMax = get(0);
        for (int i = 1; i < size(); i++) {
            if (c.compare(currMax, get(i)) > 0) {
                currMax = get(i);
            }
        }

        return currMax;
    }

}

