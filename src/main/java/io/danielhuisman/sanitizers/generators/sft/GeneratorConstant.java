package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharConstant;
import theory.characters.CharFunc;
import theory.characters.CharPred;
import transducers.sft.SFTInputMove;
import transducers.sft.SFTMove;

import java.util.*;
import java.util.stream.Collectors;

public class GeneratorConstant extends SFTGenerator<String> {

    @Override
    public String getName() {
        return "constant";
    }

    @Override
    public SFTWrapper generate(String input) throws TimeoutException {
        List<SFTMove<CharPred, CharFunc, Character>> transitions = new LinkedList<>();
        Map<Integer, Set<List<Character>>> finalStatesAndTails = new HashMap<>();

        // Convert input to character constants
        List<CharFunc> chars = input.chars().mapToObj((c) -> new CharConstant((char) c)).collect(Collectors.toList());

        // Add transition with constant output
        transitions.add(new SFTInputMove<>(0, 1, SFTWrapper.ALGEBRA.True(), chars));

        // Add true transition to self without output
        transitions.add(new SFTInputMove<>(1, 1, SFTWrapper.ALGEBRA.True(), List.of()));

        // Mark last state as final
        finalStatesAndTails.put(1, new HashSet<>());

        return new SFTWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("constant_cheese", generate("cheese")));

        return examples;
    }

    @Override
    public String format(String input) {
        return String.format("constant %s", input);
    }
}
