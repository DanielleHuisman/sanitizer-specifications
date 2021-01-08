package io.danielhuisman.sanitizers.generators.sfa;

import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorRangeList extends SFAGenerator<Collection<Pair<CharPred, Integer>>> {
    @Override
    public String getName() {
        return "range-list";
    }

    @Override
    public SFAWrapper generate(Collection<Pair<CharPred, Integer>> input) throws TimeoutException {
        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        Collection<Integer> finalStates = new HashSet<>();

        // Add transitions for each range
        int index = 0;
        for (Pair<CharPred, Integer> inputRange : input) {
            // Add transitions for the specified amount of this range
            for (int i = 0; i < inputRange.getRight(); i++) {
                transitions.add(new SFAInputMove<>(index, index + 1, inputRange.getLeft()));
                index++;
            }
        }

        // Mark last state as final
        finalStates.add(index);

        return new SFAWrapper(transitions, 0, finalStates, false);
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("range1", generate(
                List.of(
                        Pair.of(new CharPred('A', 'Z'), 3),
                        Pair.of(new CharPred('-'), 1),
                        Pair.of(new CharPred('0', '9'), 1)
                )
        )));

        return examples;
    }

    @Override
    public String format(Collection<Pair<CharPred, Integer>> input) {
        return String.format("[%s]", input
                .stream()
                .map(inputRange -> String.format("%s x%d", inputRange.getLeft().toString(), inputRange.getRight()))
                .collect(Collectors.joining(", ")));
    }
}
