package io.danielhuisman.sanitizers.benchmarks.sfa;

import com.google.common.base.Strings;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import org.apache.commons.lang3.tuple.Pair;

public class BenchmarkGeneratorWord extends BenchmarkGeneratorSFA<Pair<GeneratorWord.Operator, String>, Pair<GeneratorWord.Operator, String>> {

    public BenchmarkGeneratorWord() {
        super(new GeneratorWord());
    }

    @Override
    public Pair<GeneratorWord.Operator, String> generate(Pair<GeneratorWord.Operator, String> parameter, int index) {
        String s = parameter.getRight();
        return Pair.of(parameter.getLeft(), Strings.repeat(s, (int) Math.ceil(index / (double) s.length())).substring(0, index));
    }
}
