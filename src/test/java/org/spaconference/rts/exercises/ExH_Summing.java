package org.spaconference.rts.exercises;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spaconference.rts.runner.ExampleRunner;

import java.util.function.ToIntFunction;
import java.util.stream.IntStream;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.spaconference.rts.runner.ExampleRunner.Way;


@RunWith(ExampleRunner.class)
public class ExH_Summing {

    @Way
    public static int oldWay(int[] ints) {
        int result = 0;
        for (int i : ints) {
            result += i;
        }
        return result;
    }

    @Way
    public static int step1_introduceStream(int[] ints) {
        int result = 0;
        IntStream stream = IntStream.of(ints);
        for (int i : (Iterable<Integer>) stream::iterator) {
            result += i;
        }
        return result;
    }

    @Way
    public static int step2_forEach(int[] ints) {
        final int[] result = {0};
        IntStream.of(ints).forEach(i -> result[0] += i);
        return result[0];
    }

    @Way
    public static int step3_reduce(int[] ints) {
        return IntStream.of(ints).reduce(0, (a, b) -> a + b);
    }

    @Way
    public static int step4_reduceMethodReference(int[] ints) {
        return IntStream.of(ints).reduce(0, Math::sum);
    }

    private static class Math {
        private static int sum(int a, int b) {
            return a + b;
        }
    }

    @Way
    public static int step5_reduceMethodReferenceInteger(int[] ints) {
        return IntStream.of(ints).reduce(0, Integer::sum);
    }

    @Way
    public static int step6_sum(int[] ints) {
        return IntStream.of(ints).sum();
    }

    @Test
    public void sums_array_elements(ToIntFunction<int[]> f) {
        assertThat(f.applyAsInt(new int[]{1, 2, 3, 4, 5}), equalTo(15));
    }

    @Test
    public void is_0_for_empty_array(ToIntFunction<int[]> f) {
        assertThat(f.applyAsInt(new int[0]), equalTo(0));
    }
}
