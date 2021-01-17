package io.danielhuisman.sanitizers.benchmarks;

import io.danielhuisman.sanitizers.benchmarks.sfa.BenchmarkGeneratorLength;
import io.danielhuisman.sanitizers.benchmarks.sfa.BenchmarkGeneratorRange;
import io.danielhuisman.sanitizers.benchmarks.sfa.BenchmarkGeneratorWord;
import io.danielhuisman.sanitizers.benchmarks.sft.BenchmarkGeneratorPad;
import io.danielhuisman.sanitizers.benchmarks.sft.BenchmarkGeneratorReplaceWord;
import io.danielhuisman.sanitizers.benchmarks.sft.BenchmarkGeneratorTrim;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import io.danielhuisman.sanitizers.util.Util;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

public class Benchmarks {

    public static void main(String[] args) {
        try {
            BenchmarkGeneratorLength benchmarkLength = new BenchmarkGeneratorLength();
//            benchmarkLength.benchmarkSpeed(0, 100000, 1000, 5, GeneratorRange.Operator.EQUALS);
//            benchmarkLength.benchmarkSpeed(0, 100000, 1000, 5, GeneratorRange.Operator.GREATER_THAN_OR_EQUALS);
            benchmarkLength.benchmarkSize("gte", 0, 1000, 1, GeneratorRange.Operator.GREATER_THAN_OR_EQUALS);

            BenchmarkGeneratorRange benchmarkRange = new BenchmarkGeneratorRange();
            benchmarkRange.benchmarkSize("gte", 0, 1000, 1, Pair.of(
                    GeneratorRange.Operator.GREATER_THAN_OR_EQUALS, new CharPred('a', 'z')
            ));

            BenchmarkGeneratorWord benchmarkWord = new BenchmarkGeneratorWord();
            benchmarkWord.benchmarkSize("contains",0, 1000, 1, Pair.of(
                    GeneratorWord.Operator.CONTAINS, Util.charRangeAsString('a', 'z')
            ));

            BenchmarkGeneratorTrim benchmarkTrim = new BenchmarkGeneratorTrim();
            benchmarkTrim.benchmarkSize("trim",0, 1000, 1, null);

            BenchmarkGeneratorPad benchmarkPad = new BenchmarkGeneratorPad();
            benchmarkPad.benchmarkSize("a-z", 0, 1000, 1, Pair.of(Util.charRangeAsString('a', 'z'), false));

            BenchmarkGeneratorReplaceWord benchmarkReplaceWord = new BenchmarkGeneratorReplaceWord();
            benchmarkReplaceWord.benchmarkSize("a-z",1, 1000, 1, Pair.of(
                    Util.charRangeAsString('a', 'z'), "replacement"
            ));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
