package io.danielhuisman.sanitizers.benchmarks.sfa;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorLength;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import org.apache.commons.lang3.tuple.Pair;

public class BenchmarkGeneratorLength extends BenchmarkGeneratorSFA<Pair<GeneratorRange.Operator, Integer>, GeneratorRange.Operator> {

    public BenchmarkGeneratorLength() {
        super(new GeneratorLength());
    }

    @Override
    public Pair<GeneratorRange.Operator, Integer> generate(GeneratorRange.Operator parameter, int index) {
        return Pair.of(parameter, index);
    }
}
