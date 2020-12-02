package io.danielhuisman.sanitizers.generators.sfa;

import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

public class GeneratorLength extends SFAGenerator<Pair<GeneratorLength.Operator, Integer>> {

    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        LESS_THAN,
        LESS_THAN_OR_EQUALS,
        GREATER_THAN,
        GREATER_THAN_OR_EQUALS
    }

    @Override
    public SFAWrapper generate(Pair<GeneratorLength.Operator, Integer> input) throws TimeoutException {
        Operator operator = input.getLeft();
        int length = input.getRight();

        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        Collection<Integer> finalStates = new HashSet<>();

        // Generate length amount of states with true transitions between them
        for (int i = 0; i < length; i++) {
            transitions.add(new SFAInputMove<>(i, i + 1, SFAWrapper.ALGEBRA.True()));
        }

        if (operator == Operator.EQUALS || operator == Operator.LESS_THAN_OR_EQUALS || operator == Operator.GREATER_THAN_OR_EQUALS) {
            // Mark length state as final
            finalStates.add(length);
        }

        if (operator == Operator.NOT_EQUALS || operator == Operator.LESS_THAN || operator == Operator.LESS_THAN_OR_EQUALS) {
            // Mark all states except length as final
            for (int i = 0; i < length; i++) {
                finalStates.add(i);
            }
        }

        if (operator == Operator.NOT_EQUALS || operator == Operator.GREATER_THAN || operator == Operator.GREATER_THAN_OR_EQUALS) {
            // Add final state after length with transition to self
            finalStates.add(length + 1);
            transitions.add(new SFAInputMove<>(length, length + 1, SFAWrapper.ALGEBRA.True()));
            transitions.add(new SFAInputMove<>(length + 1, length + 1, SFAWrapper.ALGEBRA.True()));
        }

        return new SFAWrapper(transitions, 0, finalStates);
    }

    public static void main(String[] args) throws IOException, TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        SFAWrapper sfa;
        for (Operator operator : Operator.values()) {
            String name = operator.name().toLowerCase() + "_0";

            sfa = generator.generate(Pair.of(operator, 0));
            sfa.createDotFile(name, "length/");

            System.out.println(name);
            System.out.println("0: " + sfa.accepts(""));
            System.out.println("1: " + sfa.accepts("a"));
            System.out.println("2: " + sfa.accepts("aa"));
            System.out.println("3: " + sfa.accepts("abc"));
            System.out.println("4: " + sfa.accepts("fghj"));
            System.out.println();
        }
    }
}
