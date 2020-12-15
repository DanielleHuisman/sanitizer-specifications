package io.danielhuisman.sanitizers;

import java.util.function.Function;

public class Util {

    public static String indent(String s) {
        return s.replaceAll("(?m)^", "    ");
    }

    @FunctionalInterface
    public interface FunctionWithException<T, R, E extends Exception> {

        R apply(T t) throws E;
    }

    public static <T, R, E extends Exception> Function<T, R> wrapper(FunctionWithException<T, R, E> fe) {
        return arg -> {
            try {
                return fe.apply(arg);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        };
    }
}
