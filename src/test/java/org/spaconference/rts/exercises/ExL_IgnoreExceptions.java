package org.spaconference.rts.exercises;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spaconference.rts.runner.ExampleRunner;
import org.spaconference.rts.runner.ExampleRunner.Way;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(ExampleRunner.class)
public class ExL_IgnoreExceptions {

    @Way
    public static List<URL> oldWay(List<String> strings) {
        List<URL> uris = new ArrayList<>();
        for (String string : strings) {
            try {
                uris.add(new URL(string));
            } catch (MalformedURLException ignored) {
            }
        }
        return uris;
    }

    @Way
    public static List<URL> step1_introduceStream(List<String> strings) {
        List<URL> uris = new ArrayList<>();
        for (String string : (Iterable<String>) strings.stream()::iterator) {
            try {
                uris.add(new URL(string));
            } catch (MalformedURLException ignored) {
            }
        }
        return uris;
    }

    @Way
    public static List<URL> step2_forEach(List<String> strings) {
        List<URL> uris = new ArrayList<>();
        strings.stream().forEach(string -> {
            try {
                uris.add(new URL(string));
            } catch (MalformedURLException ignored) {
            }
        });
        return uris;
    }

    @Way
    public static List<URL> step3a_mapCollectNull(List<String> strings) {
        return strings.stream().
                map(string -> {
                    try {
                        return new URL(string);
                    } catch (MalformedURLException ignored) {
                        return null;
                    }
                }).
                filter(u -> u != null).
                collect(Collectors.toList());
    }

    @Way
    public static List<URL> step3b_mapCollectOptional(List<String> strings) {
        return strings.stream().
                map(string -> {
                    try {
                        return Optional.of(new URL(string));
                    } catch (MalformedURLException ignored) {
                        return Optional.<URL>empty();
                    }
                }).filter(Optional::isPresent).
                map(Optional::get).
                collect(Collectors.toList());
    }

    @Way
    public static List<URL> step3c_flatMapCollect(List<String> strings) {
        return strings.stream().flatMap(string -> {
            try {
                return Stream.of(new URL(string));
            } catch (MalformedURLException ignored) {
                return Stream.empty();
            }
        }).collect(Collectors.toList());
    }

    @Way
    public static List<URL> step4_wrapIgnoreException(List<String> strings) {
        return strings.stream().flatMap(ignoreExceptions(URL::new)).collect(Collectors.toList());
    }

    private static <T, R> Function<T, Stream<R>> ignoreExceptions(FailingFunction<T, R> factory) {
        return t -> {
            try {
                return Stream.of(factory.apply(t));
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception ignored) {
                return Stream.empty();
            }
        };
    }

    @FunctionalInterface
    public interface FailingFunction<T, R> {
        R apply(T t) throws Exception;
    }

    @Test
    public void good_urls_pass(UrlParser f) throws MalformedURLException {
        List<String> good_uris = asList(
                "http://example.com/example_a",
                "http://example.com/example_b",
                "http://example.com/example_c");

        assertThat(f.apply(good_uris), equalTo(asList(
                new URL("http://example.com/example_a"),
                new URL("http://example.com/example_b"),
                new URL("http://example.com/example_c")
        )));
    }

    @Test
    public void bad_urls_are_skipped(UrlParser f) throws MalformedURLException {
        List<String> mixed_uris = asList(
                "http://example.com/good",
                "example.com/bad",
                "http://example.com/good2");

        assertThat(f.apply(mixed_uris), equalTo(asList(
                new URL("http://example.com/good"),
                new URL("http://example.com/good2")
        )));
    }


    @FunctionalInterface
    public interface UrlParser {
        List<URL> apply(List<String> value) throws MalformedURLException;
    }
}
