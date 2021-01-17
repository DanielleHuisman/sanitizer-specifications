package io.danielhuisman.sanitizers.benchmarks;

import io.danielhuisman.sanitizers.benchmarks.sfa.BenchmarkGeneratorLength;
import io.danielhuisman.sanitizers.benchmarks.sfa.BenchmarkGeneratorRange;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

public class Benchmarks {

    public static void main(String[] args) {
        try {
            BenchmarkGeneratorLength benchmarkLength = new BenchmarkGeneratorLength();
//            benchmarkLength.benchmarkSpeed(0, 100000, 1000, 5, GeneratorRange.Operator.EQUALS);
//            benchmarkLength.benchmarkSpeed(0, 100000, 1000, 5, GeneratorRange.Operator.GREATER_THAN_OR_EQUALS);
            benchmarkLength.benchmarkSize(0, 1000, 1, GeneratorRange.Operator.GREATER_THAN_OR_EQUALS);

            BenchmarkGeneratorRange benchmarkRange = new BenchmarkGeneratorRange();
            benchmarkRange.benchmarkSize(0, 1000, 1, Pair.of(
                    GeneratorRange.Operator.GREATER_THAN_OR_EQUALS, new CharPred('a', 'z')
            ));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
