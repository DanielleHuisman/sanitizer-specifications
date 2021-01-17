package io.danielhuisman.sanitizers.benchmarks.sft;

import com.google.common.base.Strings;
import io.danielhuisman.sanitizers.generators.sft.GeneratorReplaceWord;
import org.apache.commons.lang3.tuple.Pair;

public class BenchmarkGeneratorReplaceWord extends BenchmarkGeneratorSFT<Pair<String, String>, Pair<String, String>> {

    public BenchmarkGeneratorReplaceWord() {
        super(new GeneratorReplaceWord());
    }

    @Override
    public Pair<String, String> generate(Pair<String, String> parameter, int index) {
        String s = parameter.getLeft();
        return Pair.of(Strings.repeat(s, (int) Math.ceil(index / (double) s.length())).substring(0, index), parameter.getRight());
    }
}
