package io.danielhuisman.sanitizers.benchmarks.sfa;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import theory.characters.CharPred;

public class BenchmarkGeneratorRange
        extends BenchmarkGeneratorSFA<Triple<GeneratorRange.Operator, Integer, CharPred>, Pair<GeneratorRange.Operator, CharPred>> {

    public BenchmarkGeneratorRange() {
        super(new GeneratorRange());
    }

    @Override
    public Triple<GeneratorRange.Operator, Integer, CharPred> generate(Pair<GeneratorRange.Operator, CharPred> parameter, int index) {
        return Triple.of(parameter.getLeft(), index, parameter.getRight());
    }
}
