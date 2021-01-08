package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.sat4j.specs.TimeoutException;

import java.util.*;

public class GeneratorLength extends SFAGenerator<Pair<GeneratorRange.Operator, Integer>> {

    private final GeneratorRange generatorRange;

    public GeneratorLength() {
        generatorRange = new GeneratorRange();
    }

    @Override
    public String getName() {
        return "length";
    }

    @Override
    public SFAWrapper generate(Pair<GeneratorRange.Operator, Integer> input) throws TimeoutException {
        return generatorRange.generate(Triple.of(input.getLeft(), input.getRight(), SFAWrapper.ALGEBRA.True()));
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        // Generate SFAs for all operators with length 3
        for (GeneratorRange.Operator operator : GeneratorRange.Operator.values()) {
            String name = operator.name().toLowerCase() + "_3";
            examples.add(Pair.of(name, generate(Pair.of(operator, 3))));
        }

        return examples;
    }

    @Override
    public String format(Pair<GeneratorRange.Operator, Integer> input) {
        return String.format("%s %d", input.getLeft().name().toLowerCase(), input.getRight());
    }
}
