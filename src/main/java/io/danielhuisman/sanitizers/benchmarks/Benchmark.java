package io.danielhuisman.sanitizers.benchmarks;

import io.danielhuisman.sanitizers.generators.Generator;
import org.sat4j.specs.TimeoutException;

public abstract class Benchmark<I, O, P> {

    protected final Generator<I, O> generator;

    public Benchmark(Generator<I, O> generator) {
        this.generator = generator;
    }

    public abstract I generate(P parameter, int index);

    public void benchmark(int minimum, int maximum, int step, int tries, P parameter) throws TimeoutException {
        System.out.printf("=== Benchmark of \"%s\" ===%n", generator.getName());
        System.out.printf("Parameter: %s%n%n", parameter.toString());

        long start;
        long end;
        long difference;
        long total;

        for (int index = minimum; index <= maximum; index += step) {
            total = 0;
            for (int i = 0; i < tries; i++) {
                start = System.nanoTime();
                generator.generate(generate(parameter, index));
                end = System.nanoTime();

                difference = end - start;
                total += difference;
            }

            System.out.printf("%d: %f ms%n", index, (total / (long) tries) / 1_000_000F);
        }

        System.out.println();
    }
}
