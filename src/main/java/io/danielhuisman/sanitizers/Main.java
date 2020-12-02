package io.danielhuisman.sanitizers;

import automata.sfa.SFA;
import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;
import theory.intervals.UnaryCharIntervalSolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException, TimeoutException {
        Files.createDirectories(Paths.get("dot"));

        UnaryCharIntervalSolver ba = new UnaryCharIntervalSolver();

        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        Collection<Integer> finalStates = new HashSet<>();

        finalStates.add(3);
        transitions.add(new SFAInputMove<>(0, 1, new CharPred('a')));
        transitions.add(new SFAInputMove<>(1, 2, new CharPred('b')));
        transitions.add(new SFAInputMove<>(2, 3, new CharPred('c')));
        transitions.add(new SFAInputMove<>(3, 3, new CharPred('c')));
        transitions.add(new SFAInputMove<>(0, 3, new CharPred('0', '9')));

        SFA<CharPred, Character> sfa = SFA.MkSFA(transitions, 0, finalStates, ba);
        sfa.createDotFile("full", "dot/");

        System.out.println(sfa.accepts(List.of('a', 'a'), ba));

        System.out.println(sfa.accepts(List.of('a', 'b', 'c'), ba));

        System.out.println(sfa.accepts(List.of('1'), ba));

        System.out.println(sfa.accepts(List.of('8', 'c', 'c'), ba));
    }
}
