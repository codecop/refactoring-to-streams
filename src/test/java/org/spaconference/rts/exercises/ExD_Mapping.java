package org.spaconference.rts.exercises;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spaconference.rts.runner.ExampleRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.spaconference.rts.runner.ExampleRunner.Way;


@RunWith(ExampleRunner.class)
public class ExD_Mapping {

    @Way
    public static List<Integer> oldWay(List<String> strings) {
        List<Integer> result = new ArrayList<>();
        for (String string : strings) {
            result.add(Integer.parseInt(string));
        }
        return result;
    }

    @Way
    public static List<Integer> step1_introduceStream(List<String> strings) {
        List<Integer> result = new ArrayList<>();
        Stream<String> stream = strings.stream();
        for (String string : (Iterable<String>) stream::iterator) {
            result.add(Integer.parseInt(string));
        }
        return result;
    }

    @Way
    public static List<Integer> step2_map(List<String> strings) {
        List<Integer> result = new ArrayList<>();
        Stream<Integer> stream = strings.stream().map(string -> Integer.parseInt(string));
        for (Integer i : (Iterable<Integer>) stream::iterator) {
            result.add(i);
        }
        return result;
    }

    @Way
    public static List<Integer> step3_collect(List<String> strings) {
        return strings.stream().
                map(Integer::parseInt).
                collect(Collectors.toList());
    }

    @Test
    public void test(Function<List<String>, List<Integer>> f) {
        assertThat(f.apply(asList("2", "3", "5", "7")), equalTo(asList(2, 3, 5, 7)));
    }
}
