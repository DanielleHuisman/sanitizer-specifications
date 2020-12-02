package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

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

        SFAWrapper combinedSfa = null;
        for (String word : words) {
            // Generate SFA for a word
            SFAWrapper sfa = generatorWord.generate(Pair.of(operator, word));

            // Combine previous SFA with SFA for the word (union of intersection)
            if (combinedSfa == null) {
                combinedSfa = sfa;
            } else {
                combinedSfa = intersection ? combinedSfa.intersectionWith(sfa) : combinedSfa.unionWith(sfa);
            }
        }

        // Minimize the combined SFA
        return combinedSfa.minimize();
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
}
