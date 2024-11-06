package deque;
import jh61b.utils.Reflection;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static com.google.common.truth.Truth.assertThat;
import static com.google.common.truth.Truth.assertWithMessage;

public class ArrayDequeTest {

    @Test
    @DisplayName("ArrayDeque has no fields besides backing array and primitives")
    void noNonTrivialFields() {
        List<Field> badFields = Reflection.getFields(ArrayDeque.class)
                .filter(f -> !(f.getType().isPrimitive() || f.getType().equals(Object[].class) || f.isSynthetic()))
                .toList();

        assertWithMessage("Found fields that are not array or primitives").that(badFields).isEmpty();
    }

    @Test
    void addTest() {
        ArrayDeque<Integer> d = new ArrayDeque<>();

        // Normal case
        d.addFirst(1);
        d.addFirst(2);
        d.addFirst(3);

        // Add from the last
        d.addLast(4);
        d.addLast(5);
        d.addLast(6);

        // Mix Add first and last.
        ArrayDeque<Integer> d2 = new ArrayDeque<>();
        d2.addLast(1); // [1, x, x, x, x, x, x, x]
        d2.addFirst(2); // [1, x, x, x, x, x, x, 2]
        d2.addLast(3);  // [1, 3, x, x, x, x, x, 2]
        d2.addFirst(4); // [1, 3, x, x, x, x, 4, 2]
        d2.addFirst(5);
        d2.addLast(6); // [5, 4, 2, 1, 3, 6]
    }

    @Test
    void getTest() {
        ArrayDeque<Integer> d2 = new ArrayDeque<>();

        // Edge case.
        assertThat(d2.get(23)).isNull();
        assertThat(d2.get(-3)).isNull();

        // Normal case.
        d2.addLast(1); // [1, x, x, x, x, x, x, x]
        d2.addFirst(2); // [1, x, x, x, x, x, x, 2]
        d2.addLast(3);  // [1, 3, x, x, x, x, x, 2]
        d2.addFirst(4); // [1, 3, x, x, x, x, 4, 2]
        d2.addFirst(5);
        d2.addLast(6); // [5, 4, 2, 1, 3, 6]

        assertThat(d2.get(23)).isNull();
        assertThat(d2.get(-3)).isNull();
        assertThat(d2.get(0)).isEqualTo(5);
        assertThat(d2.get(1)).isEqualTo(4);
        assertThat(d2.get(2)).isEqualTo(2);
        assertThat(d2.get(3)).isEqualTo(1);
        assertThat(d2.get(4)).isEqualTo(3);
        assertThat(d2.get(5)).isEqualTo(6);
        assertThat(d2.get(-1)).isNull();
        assertThat(d2.get(23)).isNull();
    }

    @Test
    void removeTest() {
        ArrayDeque<Integer> d = new ArrayDeque<>();

        // Edge case
        assertThat(d.removeFirst()).isNull();

        // Normal case
        d.addLast(1); // [1, x, x, x, x, x, x, x]
        d.addFirst(2); // [1, x, x, x, x, x, x, 2]
        d.addLast(3);  // [1, 3, x, x, x, x, x, 2]
        d.addFirst(4); // [1, 3, x, x, x, x, 4, 2]
        d.addFirst(5);
        d.addLast(6); // [5, 4, 2, 1, 3, 6]
        assertThat(d.removeFirst()).isEqualTo(5);
        assertThat(d.removeLast()).isEqualTo(6);
        assertThat(d.removeFirst()).isEqualTo(4);
        assertThat(d.removeLast()).isEqualTo(3);
        assertThat(d.removeFirst()).isEqualTo(2);
        assertThat(d.removeFirst()).isEqualTo(1);

        // Add first then remove last
        // [0, 1, 2, 3, 4, 5, 6]
        d.clear();
        for (int i = 6; i >= 0; i--) {
            d.addFirst(i);
        }
        for (int i = 6; i >= 0; i--) {
            assertThat(d.removeLast()).isEqualTo(i);
        }

        // Add last then remove first
        // [0, 1, 2, 3, 4, 5, 6]
        d.clear();
        for (int i = 0; i < 7; i++) {
            d.addLast(i);
        }
        for (int i = 0; i < 7; i++) {
            assertThat(d.removeFirst()).isEqualTo(i);
        }
    }

    @Test
    void resizeUpTest() {
        ArrayDeque<Integer> d = new ArrayDeque<>();
        // [0, 1 ... 998, 999, 1000, 1002, ... , 1998, 1999]
        for (int i = 999; i >= 0; i--) {
            d.addFirst(i);
        }
        for (int i = 1000; i < 2000; i++) {
            d.addLast(i);
        }

        for (int i = 0; i < 1000; i++) {
            assertThat(d.get(i)).isEqualTo(i);
        }
        for (int i = 1000; i < 2000; i++) {
            assertThat(d.get(i)).isEqualTo(i);
        }

        assertThat(d.size()).isEqualTo(2000);

    }

    @Test
    void resizeDownTest() {
        // [0, 1 ... 998, 999, 1000, 1002, ... , 1998, 1999]
        ArrayDeque<Integer> d = new ArrayDeque<>();
        for (int i = 0; i < 2000; i++) {
            d.addLast(i);
        }

        for (int i = 0; i < 1000; i++) {
            assertThat(d.removeFirst()).isEqualTo(i);
        }

        for (int i = 1999; i >= 1000; i--) {
            assertThat(d.removeLast()).isEqualTo(i);
        }

    }

    @Test
    void enhancedForLoopTest() {
        ArrayDeque<Integer> d = new ArrayDeque<>();
        for (int i = 0; i < 2000; i++) {
            d.addLast(i);
        }

        int shouldBe = 0;
        for (Integer item : d) {
            assertThat(item).isEqualTo(shouldBe);
            shouldBe += 1;
        }

        // loop after resize
        // after resize up.
        d.clear();
        for (int i = 0; i < 5; i++) {
            d.addLast(i);
        }
        shouldBe = 0;
        for (Integer item : d) {
            assertThat(item).isEqualTo(shouldBe);
            shouldBe += 1;
        }
        for (int i = 5; i < 100; i++) {
            d.addLast(i);
        }
        shouldBe = 0;
        for (Integer item : d) {
            assertThat(item).isEqualTo(shouldBe);
            shouldBe += 1;
        }

        // after resize down.
        for (int i = 0; i < 90; i++) {
            d.removeLast();
        }
        shouldBe = 0;
        for (Integer item : d) {
            assertThat(item).isEqualTo(shouldBe);
            shouldBe += 1;
        }
    }

    @Test
    void equalsTest() {
        ArrayDeque<Integer> d1 = new ArrayDeque<>();
        ArrayDeque<Integer> d2 = new ArrayDeque<>();

        for (int i = 0; i < 1000; i++) {
            d1.addLast(i);
            d2.addLast(i);
        }

        assertThat(d1.equals(d2)).isTrue();
    }
}

