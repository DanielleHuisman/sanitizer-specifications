package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.string.SFTStringWrapper;
import io.danielhuisman.sanitizers.sft.string.StringConstant;
import io.danielhuisman.sanitizers.sft.string.StringFunc;
import io.danielhuisman.sanitizers.sft.string.StringIdentity;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;
import transducers.sft.SFTInputMove;
import transducers.sft.SFTMove;

import java.util.*;

public class GeneratorReplaceWord extends SFTStringGenerator<Pair<String, String>> {

    @Override
    public String getName() {
        return "replace-word";
    }

    @Override
    public SFTStringWrapper generate(Pair<String, String> input) throws TimeoutException {
        String word = input.getLeft();
        String replacement = input.getRight();

        List<SFTMove<CharPred, StringFunc, String>> transitions = new LinkedList<>();
        Map<Integer, Set<List<String>>> finalStatesAndTails = new HashMap<>();

        // Generate states for each character
        for (int i = 0; i < word.length(); i++) {
            if (i == word.length() - 1) {
                // Add transition to last character with replacement as output
                transitions.add(new SFTInputMove<>(i, i + 1, new CharPred(word.charAt(i)), List.of(
                        new StringConstant(replacement)
                )));
            } else {
                // Add transition to next character without output
                transitions.add(new SFTInputMove<>(i, i + 1, new CharPred(word.charAt(i)), List.of()));
            }

            // Add transition back to the start if the character does not match
            transitions.add(new SFTInputMove<>(i, 0, SFTStringWrapper.ALGEBRA.MkNot(new CharPred(word.charAt(i))), List.of(
                    // TODO: this probably does not work
                    new StringConstant(word.substring(0, i)),
                    new StringIdentity()
            )));
        }

        // TODO: should other states also be final? Probably
        // TODO: deal with transitions with double the matched input character
        // TODO: deal with transitions from last state back to earlier states
        // TODO: transitions back to the first state should also output the half matched input
        // TODO: SFTStringWrapper is probably not really needed, the normal library could already support it, might just require a bit more wrapper code to make it convenient to use

        transitions.add(new SFTInputMove<>(word.length(), 0, SFTStringWrapper.ALGEBRA.MkNot(new CharPred(word.charAt(0))), List.of(
                new StringIdentity()
        )));
        transitions.add(new SFTInputMove<>(word.length(), 1, new CharPred(word.charAt(0)), List.of()));

        // Mark first and last states as final
        finalStatesAndTails.put(0, new HashSet<>());
        finalStatesAndTails.put(word.length(), new HashSet<>());

        return new SFTStringWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTStringWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTStringWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("abc_to_def", generate(Pair.of("abc", "def"))));

        return examples;
    }

    @Override
    public String format(Pair<String, String> input) {
        return String.format("\"%s\" -> \"%s\"", StringEscapeUtils.escapeJava(input.getLeft()), StringEscapeUtils.escapeJava(input.getRight()));
    }
}
