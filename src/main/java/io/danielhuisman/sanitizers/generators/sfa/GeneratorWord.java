package io.danielhuisman.sanitizers.generators.sfa;

import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.util.*;

public class GeneratorWord extends SFAGenerator<Pair<GeneratorWord.Operator, String>> {

    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        CONTAINS,
        NOT_CONTAINS
    }

    @Override
    public String getName() {
        return "word";
    }

    @Override
    public SFAWrapper generate(Pair<GeneratorWord.Operator, String> input) throws TimeoutException {
        Operator operator = input.getLeft();
        String word = input.getRight();

        Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
        Collection<Integer> finalStates = new HashSet<>();

        // Generate states for each character with transitions matching the character
        for (int i = 0; i < word.length(); i++) {
            transitions.add(new SFAInputMove<>(i, i + 1, new CharPred(word.charAt(i))));
        }

        if (operator == Operator.EQUALS || operator == Operator.CONTAINS) {
            // Mark state of last character as final
            finalStates.add(word.length());
        }

        if (operator == Operator.NOT_EQUALS || operator == Operator.NOT_CONTAINS) {
            // Mark all states except of the last character as final
            for (int i = 0; i < word.length(); i++) {
                finalStates.add(i);
            }
        }

        if (operator == Operator.NOT_EQUALS) {
            // Add final state after word with transition to self
            transitions.add(new SFAInputMove<>(word.length(), word.length() + 1, SFAWrapper.ALGEBRA.True()));
            transitions.add(new SFAInputMove<>(word.length() + 1, word.length() + 1, SFAWrapper.ALGEBRA.True()));
            finalStates.add(word.length() + 1);

            // Add transitions from character states to the final state with transitions excluding the character
            for (int i = 0; i < word.length(); i++) {
                transitions.add(new SFAInputMove<>(i, word.length() + 1, SFAWrapper.ALGEBRA.MkNot(new CharPred(word.charAt(i)))));
            }
        }

        if (operator == Operator.CONTAINS || operator == Operator.NOT_CONTAINS) {
            // Add transition to self for state of last character
            transitions.add(new SFAInputMove<>(word.length(), word.length(), SFAWrapper.ALGEBRA.True()));

            // Add transitions from character states to the initial state with transitions excluding the character
            for (int i = 0; i < word.length(); i++) {
                transitions.add(new SFAInputMove<>(i, 0, SFAWrapper.ALGEBRA.MkNot(new CharPred(word.charAt(i)))));
            }
        }

        return new SFAWrapper(transitions, 0, finalStates, false);
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        List<String> words = List.of("abc", "<script>");

        // Generate SFAs for all operators for all words
        for (Operator operator : Operator.values()) {
            for (String word : words) {
                String name = operator.name().toLowerCase() + "_" + word;
                examples.add(Pair.of(name, generate(Pair.of(operator, word))));
            }
        }

        return examples;
    }

    @Override
    public String format(Pair<Operator, String> input) {
        return String.format("%s \"%s\"", input.getLeft().name().toLowerCase(), input.getRight());
    }

    @Override
    public Pair<Operator, String> parse(String input) {
        String[] split = input.split(" ", 2);
        if (split.length == 2) {
            Optional<Operator> operator = Arrays.stream(Operator.values()).filter(op -> op.name().equalsIgnoreCase(split[0])).findFirst();
            if (operator.isPresent()) {
                // TODO: parse string properly with quotes etc
                return Pair.of(operator.get(), split[1]);
            }
        }
        return null;
    }
}
