package io.danielhuisman.sanitizers.benchmarks.sft;

import io.danielhuisman.sanitizers.generators.sft.GeneratorPad;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

public class BenchmarkGeneratorPad extends BenchmarkGeneratorSFT<Triple<Integer, String, Boolean>, Pair<String, Boolean>> {

    public BenchmarkGeneratorPad() {
        super(new GeneratorPad());
    }

    @Override
    public Triple<Integer, String, Boolean> generate(Pair<String, Boolean> parameter, int index) {
        return Triple.of(index, parameter.getLeft(), parameter.getRight());
    }
}
