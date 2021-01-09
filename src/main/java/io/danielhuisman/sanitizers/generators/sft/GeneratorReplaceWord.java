package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import io.danielhuisman.sanitizers.sft.string.SFTStringWrapper;
import io.danielhuisman.sanitizers.util.Util;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharFunc;
import theory.characters.CharOffset;
import theory.characters.CharPred;
import transducers.sft.SFTInputMove;
import transducers.sft.SFTMove;

import java.util.*;

public class GeneratorReplaceWord extends SFTGenerator<Pair<String, String>> {

    @Override
    public String getName() {
        return "replace-word";
    }

    @Override
    public SFTWrapper generate(Pair<String, String> input) throws TimeoutException {
        String word = input.getLeft();
        String replacement = input.getRight();

        List<SFTMove<CharPred, CharFunc, Character>> transitions = new LinkedList<>();
        Map<Integer, Set<List<Character>>> finalStatesAndTails = new HashMap<>();

        // Generate states for each character
        for (int i = 0; i < word.length(); i++) {
            if (i == word.length() - 1) {
                // Add transition to last character with replacement as output
                transitions.add(new SFTInputMove<>(i, i + 1, new CharPred(word.charAt(i)), Util.toCharFuncList(replacement)));
            } else {
                // Add transition to next character without output
                transitions.add(new SFTInputMove<>(i, i + 1, new CharPred(word.charAt(i)), List.of()));
            }

            // Add transition back to the start if the character does not match this character or the first character
            List<CharFunc> output = Util.toCharFuncList(word.substring(0, i));
            output.add(CharOffset.IDENTITY);
            transitions.add(new SFTInputMove<>(i,0, SFTStringWrapper.ALGEBRA.MkNot(
                    SFTStringWrapper.ALGEBRA.MkOr(new CharPred(word.charAt(0)), new CharPred(word.charAt(i)))
            ), output));

            // Add transition back to the second state if the character matches the first character
            if (i > 0) {
                output = Util.toCharFuncList(word.substring(0, i));
                transitions.add(new SFTInputMove<>(i, 1, new CharPred(word.charAt(0)), output));
            }

            // Mark state as final
            finalStatesAndTails.put(i, new HashSet<>());
        }

        // Add transitions from the last state back to the first and second states
        transitions.add(new SFTInputMove<>(word.length(), 0, SFTStringWrapper.ALGEBRA.MkNot(new CharPred(word.charAt(0))), List.of(CharOffset.IDENTITY)));
        transitions.add(new SFTInputMove<>(word.length(), 1, new CharPred(word.charAt(0)), List.of()));

        // Mark last state as final
        finalStatesAndTails.put(word.length(), new HashSet<>());

        return new SFTWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("abc_to_def", generate(Pair.of("abc", "def"))));

        return examples;
    }

    @Override
    public String format(Pair<String, String> input) {
        return String.format("\"%s\" -> \"%s\"", StringEscapeUtils.escapeJava(input.getLeft()), StringEscapeUtils.escapeJava(input.getRight()));
    }
}
