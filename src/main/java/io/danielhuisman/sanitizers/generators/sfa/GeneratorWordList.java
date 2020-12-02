package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class GeneratorWordList extends SFAGenerator<Pair<GeneratorWord.Operator, Collection<String>>> {


    private final GeneratorWord generatorWord;

    public GeneratorWordList() {
        generatorWord = new GeneratorWord();
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

    public static void main(String[] args) throws IOException, TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        SFAWrapper sfa = generator.generate(Pair.of(GeneratorWord.Operator.NOT_EQUALS, List.of("abc", "def")));
        sfa.createDotFile("combined", "word-list/");

        System.out.println("abc: " + sfa.accepts("abc"));
        System.out.println("def: " + sfa.accepts("def"));
        System.out.println("qabcdefz: " + sfa.accepts("qabcdefz"));
        System.out.println("qed: " + sfa.accepts("qed"));
    }
}
