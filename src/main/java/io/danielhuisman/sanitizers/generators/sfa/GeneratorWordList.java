package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.Util;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorWordList extends SFAGenerator<Pair<GeneratorWord.Operator, Collection<String>>> {

    private final GeneratorWord generatorWord;

    public GeneratorWordList() {
        generatorWord = new GeneratorWord();
    }

    @Override
    public String getName() {
        return "word-list";
    }

    @Override
    public SFAWrapper generate(Pair<GeneratorWord.Operator, Collection<String>> input) throws TimeoutException {
        GeneratorWord.Operator operator = input.getLeft();
        Collection<String> words = input.getRight();

        // Match the empty word if no words were provided
        if (words.size() == 0) {
            return generatorWord.generate(Pair.of(operator, ""));
        }

        // Use intersection for negative operators
        boolean intersection = operator == GeneratorWord.Operator.NOT_EQUALS || operator == GeneratorWord.Operator.NOT_CONTAINS;

        // Generate SFAs for each word
        var sfas = words
                .stream()
                .map(Util.wrapper(word -> generatorWord.generate(Pair.of(operator, word))))
                .collect(Collectors.toList());

        // Combine SFAs using intersection or union
        return intersection ? SFAWrapper.intersection(sfas) : SFAWrapper.union(sfas);
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        List<String> words = List.of("abc", "def");

        // Generate SFAs for all operators with "abc" and "def"
        for (GeneratorWord.Operator operator : GeneratorWord.Operator.values()) {
            String name = operator.name().toLowerCase();
            examples.add(Pair.of(name, generate(Pair.of(operator, words))));
        }

        return examples;
    }

    @Override
    public String format(Pair<GeneratorWord.Operator, Collection<String>> input) {
        return String.format("%s [%s]", input.getLeft().name().toLowerCase(), String.join(", ", input.getRight()));
    }

    @Override
    public Pair<GeneratorWord.Operator, Collection<String>> parse(String input) {
        return null;
    }
}
