package org.spaconference.rts.exercises;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.spaconference.rts.runner.ExampleRunner;
import org.spaconference.rts.runner.ExampleRunner.Way;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;


@RunWith(ExampleRunner.class)
public class ExK_AbortOnException {

    @Way
    public static List<URL> oldWay(List<String> strings) throws MalformedURLException {
        List<URL> uris = new ArrayList<>();
        for (String string : strings) {
            uris.add(new URL(string));
        }
        return uris;
    }

    @Way
    public static List<URL> step1_introduceStream(List<String> strings) throws MalformedURLException {
        List<URL> uris = new ArrayList<>();
        for (String string : (Iterable<String>) strings.stream()::iterator) {
            uris.add(new URL(string));
        }
        return uris;
    }

    @Way
    public static List<URL> step2_forEachWithRethrow(List<String> strings) throws MalformedURLException {
        List<URL> uris = new ArrayList<>();
        try {
            strings.stream().forEach(string -> {
                try {
                    uris.add(new URL(string));
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            });
        } catch (RuntimeException e) {
            throw (MalformedURLException) e.getCause();
        }
        return uris;
    }

    @Way
    public static List<URL> step3_collect(List<String> strings) throws MalformedURLException {
        try {
            return strings.stream().map(string -> {
                try {
                    return new URL(string);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (RuntimeException e) {
            throw (MalformedURLException) e.getCause();
        }
    }

    @Way
    public static List<URL> step4_privateException(List<String> strings) throws MalformedURLException {
        class MalformedURLRuntimeException extends RuntimeException {

            public MalformedURLRuntimeException(MalformedURLException e) {
                super(e);
            }

            @Override
            public MalformedURLException getCause() {
                return (MalformedURLException) super.getCause();
            }
        }
        try {
            return strings.stream().map(string -> {
                try {
                    return new URL(string);
                } catch (MalformedURLException e) {
                    throw new MalformedURLRuntimeException(e);
                }
            }).collect(Collectors.toList());
        } catch (MalformedURLRuntimeException e) {
            throw e.getCause();
        }
    }

//    @Way
//    public static List<URL> alternative_useLibrary(List<String> strings) throws MalformedURLException {
//        // see http://stackoverflow.com/questions/18198176/java-8-lambda-function-that-throws-exception/30246026#30246026
//        return strings.stream().
//                map(Errors.rethrow().wrap((Throwing.Function<String, URL>) URL::new)).
//                collect(Collectors.toList());
//    }

    @Way
    public static List<URL> alternative4_throwAsUnchecked(List<String> strings) throws MalformedURLException {
        return strings.stream().map(string -> {
            try {
                return new URL(string);
            } catch (MalformedURLException e) {
                throwAsUnchecked(e);
                throw new AssertionError("unreachable");
            }
        }).collect(Collectors.toList());
    }

    @SuppressWarnings("unchecked")
    private static <E extends Throwable> void throwAsUnchecked(Exception exception) throws E {
        throw (E) exception;
    }

    @Way
    public static List<URL> alternative5_wrapForThrowAsUnchecked(List<String> strings) throws MalformedURLException {
        return strings.stream().
                map(wrapAndRethrowAsUnchecked(URL::new)).
                collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface NewUrl {
        URL apply(String value) throws MalformedURLException;
    }

    private static Function<String, URL> wrapAndRethrowAsUnchecked(NewUrl factory) {
        return string -> {
            try {
                return factory.apply(string);
            } catch (MalformedURLException e) {
                throwAsUnchecked(e);
                throw new AssertionError("unreachable");
            }
        };
    }

    @Way
    public static List<URL> alternative6_FunctionalInterfaceWithDefault(List<String> strings) throws MalformedURLException {
        // see http://stackoverflow.com/a/27252163/104143
        return strings.stream().
                map((ThrowingFunction<String, URL>) URL::new).
                collect(Collectors.toList());
    }

    @FunctionalInterface
    public interface ThrowingFunction<T, R> extends Function<T, R> {

        @Override
        default R apply(T t) {
            try {
                return applyThrows(t);
            } catch (final Exception e) {
                throwAsUnchecked(e);
                throw new AssertionError("unreachable");
            }
        }

        R applyThrows(T t) throws Exception;
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

    @Test(expected = MalformedURLException.class)
    public void bad_urls_abort(UrlParser f) throws MalformedURLException {
        List<String> bad_uris = asList(
                "http://example.com/good",
                "example.com/bad",
                "http://example.com/good2");

        f.apply(bad_uris);
    }

    @FunctionalInterface
    public interface UrlParser {
        List<URL> apply(List<String> value) throws MalformedURLException;
    }
}
