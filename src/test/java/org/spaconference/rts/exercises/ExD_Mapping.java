package org.spaconference.rts.exercises;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spaconference.rts.runner.ExampleRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

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
    public static List<Integer> step1_iterateStream(List<String> strings) {
        Stream<String> stream = StreamSupport.stream(strings.spliterator(), false);
        Iterable<String> iterable = stream::iterator;
        List<Integer> result = new ArrayList<>();
        for (String string : iterable) {
            result.add(Integer.parseInt(string));
        }
        return result;
    }

    @Way
    public static List<Integer> step2_mapStream(List<String> strings) {
        Stream<Integer> stream = StreamSupport.stream(strings.spliterator(), false).
                                               map(string -> Integer.parseInt(string));
        Iterable<Integer> iterable = stream::iterator;
        List<Integer> result = new ArrayList<>();
        for (Integer i : iterable) {
            result.add(i);
        }
        return result;
    }

    @Way
    public static List<Integer> step3_mapCollect(List<String> strings) {
        return StreamSupport.stream(strings.spliterator(), false).
                map(Integer::parseInt).
                collect(Collectors.toList());
    }

    @Test
    public void test(Function<List<String>, List<Integer>> f) {
        assertThat(f.apply(asList("2", "3", "5", "7")), equalTo(asList(2, 3, 5, 7)));
    }
}
