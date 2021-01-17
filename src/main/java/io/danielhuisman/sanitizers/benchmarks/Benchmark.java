package io.danielhuisman.sanitizers.benchmarks;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.generators.Generator;
import org.sat4j.specs.TimeoutException;

public abstract class Benchmark<I, O extends AutomatonWrapper<?, ?>, P> {

    protected final Generator<I, O> generator;

    public Benchmark(Generator<I, O> generator) {
        this.generator = generator;
    }

    public abstract I generate(P parameter, int index);

    public void benchmarkSize(int minimum, int maximum, int step, P parameter) throws TimeoutException {
        System.out.printf("=== Size benchmark of \"%s\" ===%n", generator.getName());
        System.out.printf("Parameter: %s%n%n", parameter == null ? "null" : parameter.toString());

        for (int index = minimum; index <= maximum; index += step) {
            var automaton = generator.generate(generate(parameter, index));
            int states = automaton.getAutomaton().getStates().size();
            int transitions = automaton.getAutomaton().getMoves().size();

            System.out.printf("[%s] %d: %d states, %d transitions %n", generator.getName(), index, states, transitions);
        }

        System.out.println();
    }

    public void benchmarkSpeed(int minimum, int maximum, int step, int tries, P parameter) throws TimeoutException {
        System.out.printf("=== Speed benchmark of \"%s\" ===%n", generator.getName());
        System.out.printf("Parameter: %s%n%n", parameter == null ? "null" : parameter.toString());

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

            System.out.printf("[%s] %d: %f ms%n", generator.getName(), index, (total / (long) tries) / 1_000_000F);
        }

        System.out.println();
    }
}
