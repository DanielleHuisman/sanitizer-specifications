package io.danielhuisman.sanitizers.generators.sfa;

import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import io.danielhuisman.sanitizers.util.ICoercible;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.util.*;

public class GeneratorRange extends SFAGenerator<Triple<GeneratorRange.Operator, Integer, CharPred>> {

    public enum Operator implements ICoercible {
        EQUALS("="),
        NOT_EQUALS("!="),
        LESS_THAN("<"),
        LESS_THAN_OR_EQUALS("<="),
        GREATER_THAN(">"),
        GREATER_THAN_OR_EQUALS(">=");

        private final String alias;

        Operator(String alias) {
            this.alias = alias;
        }

        @Override
        public String getAlias() {
            return alias;
        }
    }

    @Override
    public String getName() {
        return "range";
    }

    @Override
    public SFAWrapper generate(Triple<GeneratorRange.Operator, Integer, CharPred> input) throws TimeoutException {
        Operator operator = input.getLeft();
        int length = input.getMiddle();
        CharPred range = input.getRight();

        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        Collection<Integer> finalStates = new HashSet<>();

        // Generate length amount of states with true transitions between them
        for (int i = 0; i < length; i++) {
            transitions.add(new SFAInputMove<>(i, i + 1, range));
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
            transitions.add(new SFAInputMove<>(length, length + 1, range));
            transitions.add(new SFAInputMove<>(length + 1, length + 1, range));
        }

        return new SFAWrapper(transitions, 0, finalStates, false);
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        // Generate SFAs for all operators with length 3
        for (Operator operator : Operator.values()) {
            String name = operator.name().toLowerCase() + "_3_a_z";
            examples.add(Pair.of(name, generate(Triple.of(operator, 3, new CharPred('a', 'z')))));
        }

        return examples;
    }

    @Override
    public String format(Triple<Operator, Integer, CharPred> input) {
        return String.format("%s %d %s", input.getLeft().name().toLowerCase(), input.getMiddle(), input.getRight().toString());
    }

    @Override
    public Triple<Operator, Integer, CharPred> parse(String input) {
//        String[] split = input.split(" ");
//        if (split.length == 2) {
//            Optional<Operator> operator = Arrays.stream(Operator.values()).filter(op -> op.name().equalsIgnoreCase(split[0])).findFirst();
//            if (operator.isPresent()) {
//                try {
//                    int length = Integer.parseInt(split[1]);
//
//                    return Pair.of(operator.get(), length);
//                } catch (NumberFormatException e) {
//                    return null;
//                }
//            }
//        }
        return null;
    }
}
