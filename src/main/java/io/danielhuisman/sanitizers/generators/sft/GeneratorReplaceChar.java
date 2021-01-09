package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
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
import java.util.stream.Collectors;

public class GeneratorReplaceChar extends SFTGenerator<Collection<Pair<CharPred, String>>> {

    @Override
    public String getName() {
        return "replace-char";
    }

    @Override
    public SFTWrapper generate(Collection<Pair<CharPred, String>> input) throws TimeoutException {
        List<SFTMove<CharPred, CharFunc, Character>> transitions = new LinkedList<>();
        Map<Integer, Set<List<Character>>> finalStatesAndTails = new HashMap<>();

        CharPred combinedRange = null;
        for (Pair<CharPred, String> replacement : input) {
            CharPred range = replacement.getLeft();
            String replacementOutput = replacement.getRight();

            // Combine the replacement range
            if (combinedRange == null) {
                combinedRange = range;
            } else {
                combinedRange = SFTWrapper.ALGEBRA.MkOr(combinedRange, range);
            }

            // Add transition to self for the range and output the replacement string
            transitions.add(new SFTInputMove<>(0, 0, range, Util.toCharFuncList(replacementOutput)));
        }

        // Add transition to self for characters whose output is unchanged
        transitions.add(new SFTInputMove<>(0, 0, SFTWrapper.ALGEBRA.MkNot(combinedRange), List.of(CharOffset.IDENTITY)));

        // Mark the only state as final
        finalStatesAndTails.put(0, new HashSet<>());

        return new SFTWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("a_b_swap", generate(
                List.of(
                        Pair.of(new CharPred('a'), "b"),
                        Pair.of(new CharPred('b'), "a")
                )
        )));

        examples.add(Pair.of("html_entities", generate(
                List.of(
                        Pair.of(new CharPred('<'), "&lt;"),
                        Pair.of(new CharPred('>'), "&gt;"),
                        Pair.of(new CharPred('&'), "&amp;")
                )
        )));

        return examples;
    }

    @Override
    public String format(Collection<Pair<CharPred, String>> input) {
        return String.format("[%s]", input
                .stream()
                .map(inputRange -> String.format("%s -> \"%s\"", inputRange.getLeft().toString(), StringEscapeUtils.escapeJava(inputRange.getRight())))
                .collect(Collectors.joining(", ")));
    }
}
