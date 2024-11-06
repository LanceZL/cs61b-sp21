package deque;

import org.junit.Test;

import java.util.Comparator;
import java.util.Random;

import static com.google.common.truth.Truth.assertThat;

public class MaxArrayTest {
    private class IntegerComparator implements Comparator<Integer> {

        @Override
        public int compare(Integer o1, Integer o2) {
            return o1 - o2;
        }
    }

    @Test
    public void comparatorTest() {
        MaxArrayDeque<Integer> d = new MaxArrayDeque<>(new IntegerComparator());
        assertThat(d.max()).isNull();

        Random random = new Random();
        int shouldBe = 0;
        for (int i = 0; i < 1000; i++) {
            int next = random.nextInt(1000);
            d.addFirst(next);
            shouldBe = Math.max(shouldBe, next);
        }

        assertThat(d.max()).isEqualTo(shouldBe);
        assertThat(d.max(new IntegerComparator())).isEqualTo(shouldBe);
    }
}
