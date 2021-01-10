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

public class GeneratorTrivial extends SFAGenerator<Boolean> {

    @Override
    public String getName() {
        return "trivial";
    }

    @Override
    public SFAWrapper generate(Boolean input) throws TimeoutException {
        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        Collection<Integer> finalStates = new HashSet<>();

        // Add true transition from the initial state to itself
        transitions.add(new SFAInputMove<>(0, 0, SFAWrapper.ALGEBRA.True()));

        // Mark the state as final if needed
        if (input) {
            finalStates.add(0);
        }

        return new SFAWrapper(transitions, 0, finalStates, false);
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("accept", generate(true)));
        examples.add(Pair.of("reject", generate(false)));

        return examples;
    }

    @Override
    public String format(Boolean input) {
        return String.format("trivial %s", input ? "accept" : "reject");
    }
}
