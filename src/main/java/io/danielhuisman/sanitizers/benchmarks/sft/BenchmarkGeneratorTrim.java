package io.danielhuisman.sanitizers.benchmarks.sft;

import io.danielhuisman.sanitizers.generators.sft.GeneratorTrim;

public class BenchmarkGeneratorTrim extends BenchmarkGeneratorSFT<Integer, Void> {

    public BenchmarkGeneratorTrim() {
        super(new GeneratorTrim());
    }

    @Override
    public Integer generate(Void parameter, int index) {
        return index;
    }
}
