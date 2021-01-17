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

    private static final int SIZE_MIN = 0;
    private static final int SIZE_MAX = 1000;
    private static final int SIZE_STEP = 1;

    private static final int SPEED_MIN = 0;
    private static final int SPEED_MAX = 100000;
    private static final int SPEED_STEP = 1000;
    private static final int SPEED_TRIES = 5;

    public static void main(String[] args) {
        try {
            // Length
            BenchmarkGeneratorLength benchmarkLength = new BenchmarkGeneratorLength();
            for (var operator : GeneratorRange.Operator.values()) {
                benchmarkLength.benchmarkSize(operator.name().toLowerCase(), SIZE_MIN, SIZE_MAX, SIZE_STEP, operator);

                benchmarkLength.benchmarkSpeed(operator.name().toLowerCase(), SPEED_MIN, SPEED_MAX, SPEED_STEP, SPEED_TRIES, operator);
            }

            // Range
            BenchmarkGeneratorRange benchmarkRange = new BenchmarkGeneratorRange();
            for (var operator : GeneratorRange.Operator.values()) {
                benchmarkRange.benchmarkSize("a-z_" + operator.name().toLowerCase(), SIZE_MIN, SIZE_MAX, SIZE_STEP, Pair.of(
                        operator, new CharPred('a', 'z')
                ));

                benchmarkRange.benchmarkSpeed("a-z_" + operator.name().toLowerCase(), SPEED_MIN, SPEED_MAX, SPEED_STEP, SPEED_TRIES, Pair.of(
                        operator, new CharPred('a', 'z')
                ));
            }

            // Word
            BenchmarkGeneratorWord benchmarkWord = new BenchmarkGeneratorWord();
            for (var operator : GeneratorWord.Operator.values()) {
                benchmarkWord.benchmarkSize("a-z_" + operator.name().toLowerCase(), SIZE_MIN, SIZE_MAX, SIZE_STEP, Pair.of(
                        operator, Util.charRangeAsString('a', 'z')
                ));

                benchmarkWord.benchmarkSpeed("a-z_" + operator.name().toLowerCase(), SPEED_MIN, SPEED_MAX, SPEED_STEP, SPEED_TRIES, Pair.of(
                        operator, Util.charRangeAsString('a', 'z')
                ));
            }

            // Trim end
            BenchmarkGeneratorTrim benchmarkTrim = new BenchmarkGeneratorTrim();
            benchmarkTrim.benchmarkSize(null, SIZE_MIN, SIZE_MAX, SIZE_STEP, null);
            benchmarkTrim.benchmarkSpeed(null, SPEED_MIN, SPEED_MAX, SPEED_STEP, SPEED_TRIES, null);

            // Pad end
            BenchmarkGeneratorPad benchmarkPad = new BenchmarkGeneratorPad();
            benchmarkPad.benchmarkSize("a-z", SIZE_MIN, SIZE_MAX, SIZE_STEP, Pair.of(Util.charRangeAsString('a', 'z'), false));
            benchmarkPad.benchmarkSpeed("a-z", SPEED_MIN, SPEED_MAX, SPEED_STEP, SPEED_TRIES, Pair.of(Util.charRangeAsString('a', 'z'), false));

            // Replace word
            BenchmarkGeneratorReplaceWord benchmarkReplaceWord = new BenchmarkGeneratorReplaceWord();
            benchmarkReplaceWord.benchmarkSize("a-z", Math.max(SIZE_MIN, 1), SIZE_MAX, SIZE_STEP, Pair.of(
                    Util.charRangeAsString('a', 'z'), "replacement"
            ));
            benchmarkReplaceWord.benchmarkSpeed("a-z", Math.max(SPEED_MIN, 1), SPEED_MAX, SPEED_STEP, SPEED_TRIES, Pair.of(
                    Util.charRangeAsString('a', 'z'), "replacement"
            ));
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
