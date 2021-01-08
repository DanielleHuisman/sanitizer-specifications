package io.danielhuisman.sanitizers.sfa;

import automata.Automaton;
import automata.sfa.SFA;
import automata.sfa.SFAMove;
import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import org.sat4j.specs.TimeoutException;
import theory.BooleanAlgebra;
import theory.characters.CharPred;
import theory.intervals.UnaryCharIntervalSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class SFAWrapper implements AutomatonWrapper<CharPred, Character> {

    public static final BooleanAlgebra<CharPred, Character> ALGEBRA = new UnaryCharIntervalSolver();

    private final SFA<CharPred, Character> sfa;

    public SFAWrapper(SFA<CharPred, Character> sfa) {
        this.sfa = sfa;
    }

    public SFAWrapper(Collection<SFAMove<CharPred, Character>> transitions, Integer initialState, Collection<Integer> finalStates) throws TimeoutException {
        this.sfa = SFA.MkSFA(transitions, initialState, finalStates, ALGEBRA);
    }

    public SFAWrapper(Collection<SFAMove<CharPred, Character>> transitions, Integer initialState, Collection<Integer> finalStates,
                      boolean remUnreachableStates) throws TimeoutException {
        this.sfa = SFA.MkSFA(transitions, initialState, finalStates, ALGEBRA, remUnreachableStates);
    }

    public SFAWrapper(Collection<SFAMove<CharPred, Character>> transitions, Integer initialState, Collection<Integer> finalStates,
                      boolean remUnreachableStates, boolean normalize) throws TimeoutException {
        this.sfa = SFA.MkSFA(transitions, initialState, finalStates, ALGEBRA, remUnreachableStates, normalize);
    }

    public SFAWrapper(Collection<SFAMove<CharPred, Character>> transitions, Integer initialState, Collection<Integer> finalStates,
                      boolean remUnreachableStates, boolean normalize, boolean keepEmpty) throws TimeoutException {
        this.sfa = SFA.MkSFA(transitions, initialState, finalStates, ALGEBRA, remUnreachableStates, normalize, keepEmpty);
    }

    @Override
    public Automaton<CharPred, Character> getAutomaton() {
        return sfa;
    }

    public SFA<CharPred, Character> getSFA() {
        return sfa;
    }

    @Override
    public void createDotFile(String fileName) throws IOException {
        Files.createDirectories(Paths.get("dot"));

        getSFA().createDotFile(fileName, "dot/");
    }

    @Override
    public void createDotFile(String fileName, String path) throws IOException {
        Files.createDirectories(Paths.get("dot/" + path));

        getSFA().createDotFile(fileName, "dot/" + path);
    }

    public boolean accepts(List<Character> input) throws TimeoutException {
        return getSFA().accepts(input, ALGEBRA);
    }

    @Override
    public boolean accepts(String input) throws TimeoutException {
        return accepts(input.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    @Override
    public String execute(String input) throws TimeoutException {
        return null;
    }

    public SFAWrapper complement() throws TimeoutException {
        return new SFAWrapper(getSFA().complement(ALGEBRA));
    }

    public SFAWrapper minimize() throws TimeoutException {
        return new SFAWrapper(getSFA().minimize(ALGEBRA));
    }

    public SFAWrapper cleanUp() throws TimeoutException {
        // Find useless state, i.e. non-final states with only incoming transitions or self transitions
        List<Integer> uselessStates = new ArrayList<>();
        for (int state : getSFA().getNonFinalStates()) {
            var moves = getSFA().getMovesFrom(state);

            if (moves.size() == 0 || (moves.size() == 1 && moves.iterator().next().to == state)) {
                uselessStates.add(state);
            }
        }

        // Exclude transitions to useless states
        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        for (var transition : getSFA().getTransitions()) {
            if (!uselessStates.contains(transition.to)) {
                transitions.add(transition);
            }
        }

        // TODO: consider compressing state numbers

        // Create the new SFA
        return new SFAWrapper(transitions, getSFA().getInitialState(), getSFA().getFinalStates());
    }

    public SFAWrapper concatenateWith(SFAWrapper other) throws TimeoutException {
        return new SFAWrapper(getSFA().concatenateWith(other.getSFA(), ALGEBRA));
    }

    public SFAWrapper unionWith(SFAWrapper other) throws TimeoutException {
        return new SFAWrapper(getSFA().unionWith(other.getSFA(), ALGEBRA));
    }

    public SFAWrapper intersectionWith(SFAWrapper other) throws TimeoutException {
        return new SFAWrapper(getSFA().intersectionWith(other.getSFA(), ALGEBRA));
    }

    public static SFAWrapper concatenate(Collection<SFAWrapper> sfas, boolean minimize) throws TimeoutException {
        if (sfas.size() == 0) {
            throw new RuntimeException("At least one SFA must be provided.");
        }

        SFAWrapper result = null;
        for (SFAWrapper sfa : sfas) {
            if (result == null) {
                result = sfa;
            } else {
                result = result.concatenateWith(sfa);
            }
        }
        return minimize ? result.minimize() : result;
    }

    public static SFAWrapper union(Collection<SFAWrapper> sfas, boolean minimize) throws TimeoutException {
        if (sfas.size() == 0) {
            throw new RuntimeException("At least one SFA must be provided.");
        }

        SFAWrapper result = null;
        for (SFAWrapper sfa : sfas) {
            if (result == null) {
                result = sfa;
            } else {
                result = result.unionWith(sfa);
            }
        }
        return minimize ? result.minimize() : result;
    }

    public static SFAWrapper intersection(Collection<SFAWrapper> sfas, boolean minimize) throws TimeoutException {
        if (sfas.size() == 0) {
            throw new RuntimeException("At least one SFA must be provided.");
        }

        SFAWrapper result = null;
        for (SFAWrapper sfa : sfas) {
            if (result == null) {
                result = sfa;
            } else {
                result = result.intersectionWith(sfa);
            }
        }
        return minimize ? result.minimize() : result;
    }
}
