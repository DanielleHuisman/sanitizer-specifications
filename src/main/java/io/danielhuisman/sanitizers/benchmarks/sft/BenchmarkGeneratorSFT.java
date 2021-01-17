package io.danielhuisman.sanitizers.benchmarks.sft;

import io.danielhuisman.sanitizers.benchmarks.Benchmark;
import io.danielhuisman.sanitizers.generators.sft.SFTGenerator;
import io.danielhuisman.sanitizers.sft.SFTWrapper;

public abstract class BenchmarkGeneratorSFT<I, P> extends Benchmark<I, SFTWrapper, P> {

    public BenchmarkGeneratorSFT(SFTGenerator<I> generator) {
        super(generator);
    }
}
