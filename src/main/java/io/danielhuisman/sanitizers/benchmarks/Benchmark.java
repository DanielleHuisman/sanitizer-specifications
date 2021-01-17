package io.danielhuisman.sanitizers.benchmarks;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.generators.Generator;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public abstract class Benchmark<I, O extends AutomatonWrapper<?, ?>, P> {

    protected final Generator<I, O> generator;

    public Benchmark(Generator<I, O> generator) {
        this.generator = generator;
    }

    public abstract I generate(P parameter, int index);

    public void benchmarkSize(String name, int minimum, int maximum, int step, P parameter) throws TimeoutException {
        System.out.printf("=== Size benchmark of \"%s\" ===%n", generator.getName());
        System.out.printf("Parameter: %s%n%n", parameter == null ? "null" : parameter.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("Number,States,Transitions\n");

        for (int index = minimum; index <= maximum; index += step) {
            var automaton = generator.generate(generate(parameter, index));
            int states = automaton.getAutomaton().getStates().size();
            int transitions = automaton.getAutomaton().getMoves().size();

            System.out.printf("[%s] %d: %d states, %d transitions %n", generator.getName(), index, states, transitions);
            sb.append(String.format("%d,%d,%d\n", index, states, transitions));
        }

        System.out.println();
        write(generator.getName(), name == null ? "size" : "size_" + name, sb.toString());
    }

    public void benchmarkSpeed(String name, int minimum, int maximum, int step, int tries, P parameter) throws TimeoutException {
        System.out.printf("=== Speed benchmark of \"%s\" ===%n", generator.getName());
        System.out.printf("Parameter: %s%n%n", parameter == null ? "null" : parameter.toString());

        StringBuilder sb = new StringBuilder();
        sb.append("Number,States,Transitions\n");

        long start;
        long end;
        long difference;
        long total;

        for (int index = minimum; index <= maximum; index += step) {
            total = 0;
            for (int i = 0; i < tries; i++) {
                var input = generate(parameter, index);
                start = System.nanoTime();
                generator.generate(input);
                end = System.nanoTime();

                difference = end - start;
                total += difference;
            }

            float time = (total / (float) tries) / 1_000_000F;
            System.out.printf("[%s] %d: %f ms%n", generator.getName(), index, time);
            sb.append(String.format("%d,%f\n", index, time));
        }

        System.out.println();
        write(generator.getName(), name == null ? "speed" : "speed_" + name, sb.toString());
    }

    private static void write(String path, String name, String content) {
        try {
            Files.createDirectories(Paths.get("benchmarks/" + path));
            Files.writeString(Paths.get("benchmarks/" + path + "/" + name + ".csv"), content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
