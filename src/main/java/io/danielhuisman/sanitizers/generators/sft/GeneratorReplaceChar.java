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
import java.util.stream.Collectors;

public class GeneratorReplaceChar extends SFTStringGenerator<Collection<Pair<CharPred, String>>> {

    @Override
    public String getName() {
        return "replace-char";
    }

    @Override
    public SFTStringWrapper generate(Collection<Pair<CharPred, String>> input) throws TimeoutException {
        List<SFTMove<CharPred, StringFunc, String>> transitions = new LinkedList<>();
        Map<Integer, Set<List<String>>> finalStatesAndTails = new HashMap<>();

        CharPred combinedRange = null;
        for (Pair<CharPred, String> replacement : input) {
            CharPred range = replacement.getLeft();
            String replacementOutput = replacement.getRight();

            // Combine the replacement range
            if (combinedRange == null) {
                combinedRange = range;
            } else {
                combinedRange = SFTStringWrapper.ALGEBRA.MkOr(combinedRange, range);
            }

            // Add transition to self for the range and output the replacement string
            List<StringFunc> output = new LinkedList<>();
            output.add(new StringConstant(replacementOutput));
            transitions.add(new SFTInputMove<>(0, 0, range, output));
        }

        // Add transition to self for characters whose output is unchanged
        List<StringFunc> output = new LinkedList<>();
        output.add(new StringIdentity());
        transitions.add(new SFTInputMove<>(0, 0, SFTStringWrapper.ALGEBRA.MkNot(combinedRange), output));

        // Mark the only state as final
        finalStatesAndTails.put(0, new HashSet<>());

        return new SFTStringWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTStringWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTStringWrapper>> examples = new LinkedList<>();

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

    @Override
    public Collection<Pair<CharPred, String>> parse(String input) {
        return null;
    }
}
