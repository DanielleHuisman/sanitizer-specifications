package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharFunc;
import theory.characters.CharOffset;
import theory.characters.CharPred;
import transducers.sft.SFTInputMove;
import transducers.sft.SFTMove;

import java.util.*;

public class GeneratorTrim extends SFTGenerator<Integer> {

    @Override
    public String getName() {
        return "trim";
    }

    @Override
    public SFTWrapper generate(Integer input) throws TimeoutException {
        int length = input;

        List<SFTMove<CharPred, CharFunc, Character>> transitions = new LinkedList<>();
        Map<Integer, Set<List<Character>>> finalStatesAndTails = new HashMap<>();

        // Generate length amount of states with true transitions between them with identity output
        for (int i = 0; i < length; i++) {
            transitions.add(new SFTInputMove<>(i, i + 1, SFTWrapper.ALGEBRA.True(), List.of(CharOffset.IDENTITY)));
            finalStatesAndTails.put(i, new HashSet<>());
        }

        // Add transition to self for final state without output
        transitions.add(new SFTInputMove<>(length, length, SFTWrapper.ALGEBRA.True(), List.of()));
        finalStatesAndTails.put(length, new HashSet<>());

        return new SFTWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("trim_3", generate(3)));

        return examples;
    }

    @Override
    public String format(Integer input) {
        return String.format("trim %d", input);
    }
}
