package io.danielhuisman.sanitizers.benchmarks.sfa;

import io.danielhuisman.sanitizers.benchmarks.Benchmark;
import io.danielhuisman.sanitizers.generators.sfa.SFAGenerator;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;

public abstract class BenchmarkGeneratorSFA<I, P> extends Benchmark<I, SFAWrapper, P> {

    public BenchmarkGeneratorSFA(SFAGenerator<I> generator) {
        super(generator);
    }
}
