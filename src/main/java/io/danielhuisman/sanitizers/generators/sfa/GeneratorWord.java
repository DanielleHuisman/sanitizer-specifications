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

public class GeneratorWord extends SFAGenerator<Pair<GeneratorWord.Operator, String>> {

    public enum Operator {
        EQUALS,
        NOT_EQUALS,
        CONTAINS,
        NOT_CONTAINS
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

    public static void main(String[] args) throws IOException, TimeoutException {
        GeneratorWord generator = new GeneratorWord();

        String[] words = new String[] {"aa", "vck", "<script>"};
        for (String word : words) {
            SFAWrapper sfa = generator.generate(Pair.of(Operator.NOT_CONTAINS, word));
            sfa.createDotFile(word, "word/");

            System.out.println(word);
            System.out.println(sfa.accepts("test"));
            System.out.println(sfa.accepts(word));
            System.out.println(sfa.accepts(word + "2"));
            System.out.println();
        }

        for (Operator operator : Operator.values()) {
            String name = operator.name().toLowerCase() + "_abc";

            SFAWrapper sfa = generator.generate(Pair.of(operator, "abc"));
            sfa.createDotFile(name, "word/");

            System.out.println(name);
            System.out.println("aa: " + sfa.accepts("aa"));
            System.out.println("abc: " + sfa.accepts("abc"));
            System.out.println("qabcq: " + sfa.accepts("qabcq"));
            System.out.println();
        }
    }
}
