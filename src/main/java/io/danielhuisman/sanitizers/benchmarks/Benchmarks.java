package io.danielhuisman.sanitizers.benchmarks;

import io.danielhuisman.sanitizers.benchmarks.sfa.BenchmarkGeneratorLength;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import org.sat4j.specs.TimeoutException;

public class Benchmarks {

    public static void main(String[] args) {
        try {
            BenchmarkGeneratorLength benchmark = new BenchmarkGeneratorLength();
            benchmark.benchmark(0, 100000, 1000, 5, GeneratorRange.Operator.EQUALS);
            benchmark.benchmark(0, 100000, 1000, 5, GeneratorRange.Operator.GREATER_THAN_OR_EQUALS);
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
