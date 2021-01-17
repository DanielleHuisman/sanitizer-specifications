package io.danielhuisman.sanitizers.generators.sft;

import com.google.common.base.Strings;
import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharFunc;
import theory.characters.CharOffset;
import theory.characters.CharPred;
import transducers.sft.SFTEpsilon;
import transducers.sft.SFTInputMove;
import transducers.sft.SFTMove;

import java.util.*;
import java.util.stream.Collectors;

public class GeneratorPad extends SFTGenerator<Triple<Integer, String, Boolean>> {

    @Override
    public String getName() {
        return "pad";
    }

    @Override
    public SFTWrapper generate(Triple<Integer, String, Boolean> input) throws TimeoutException {
        int length = input.getLeft();
        String padding = input.getMiddle();
        boolean trim = input.getRight();

        List<SFTMove<CharPred, CharFunc, Character>> transitions = new LinkedList<>();
        Map<Integer, Set<List<Character>>> finalStatesAndTails = new HashMap<>();

        // Generate length amount of states
        for (int i = 0; i < length; i++) {
            // Add true transition between this state and the next with identity output
            transitions.add(new SFTInputMove<>(i, i + 1, SFTWrapper.ALGEBRA.True(), List.of(CharOffset.IDENTITY)));

            // Add an epsilon transition between this state and the final state with padding output
            int required = length - i;
            var output = Strings
                    .repeat(padding, (int) Math.ceil(required / (double) padding.length()))
                    .substring(0, required)
                    .chars()
                    .mapToObj((c) -> (char) c)
                    .collect(Collectors.toList());
            transitions.add(new SFTEpsilon<>(i, length + 1, output));
        }

        // Add true transition to self for the second to last state with identity output
        transitions.add(new SFTInputMove<>(length, length, SFTWrapper.ALGEBRA.True(), trim ? List.of() : List.of(CharOffset.IDENTITY)));

        // Add epsilon transition between the second to last and final state without output
        transitions.add(new SFTEpsilon<>(length, length + 1, List.of()));
        finalStatesAndTails.put(length + 1, new HashSet<>());

        return new SFTWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("pad_2_ab_no_trim", generate(Triple.of(2, "ab", false))));
        examples.add(Pair.of("pad_5_a_trim", generate(Triple.of(5, "a", true))));
        examples.add(Pair.of("pad_5_ab_no_trim", generate(Triple.of(5, "ab", false))));

        return examples;
    }

    @Override
    public String format(Triple<Integer, String, Boolean> input) {
        return String.format("pad %d \"%s\" %s", input.getLeft(), StringEscapeUtils.escapeJava(input.getMiddle()), input.getRight() ? "trim" : "no trim");
    }
}
