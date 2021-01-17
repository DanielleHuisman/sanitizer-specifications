package io.danielhuisman.sanitizers.generators.sfa;

import automata.sfa.SFAEpsilon;
import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
import io.danielhuisman.sanitizers.language.RegexLanguage;
import io.danielhuisman.sanitizers.language.regex.*;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import io.danielhuisman.sanitizers.util.Util;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class GeneratorRegex extends SFAGenerator<Regex> {

    @Override
    public String getName() {
        return "regex";
    }

    @Override
    public SFAWrapper generate(Regex input) throws TimeoutException {
        return generate(input.expression).minimize().cleanUp();
    }

    private SFAWrapper generate(RegexExpression expression) throws TimeoutException {
        if (expression instanceof RegexExpressionOperator) {
            var expressionOperator = (RegexExpressionOperator) expression;

            List<SFAWrapper> expressions = expressionOperator.expressions
                    .stream()
                    .map(Util.wrapper(this::generate))
                    .collect(Collectors.toList());

            System.out.println("combine " + expressionOperator.operator + ":");
            System.out.println(expressions
                    .stream()
                    .map(Util.wrapper((s) -> s.cleanUp().getSFA().getTransitions().toString()))
                    .collect(Collectors.joining("\n"))
            );

            var combined = expressionOperator.operator == RegexExpressionOperator.Operator.OR ?
                    SFAWrapper.union(expressions, false) : SFAWrapper.concatenate(expressions, false);
            System.out.println("result:");
            System.out.println(combined.cleanUp().getSFA().getTransitions());
            System.out.println();
            return combined;
        } else if (expression instanceof RegexExpressionQuantifier) {
            var expressionQuantifier = (RegexExpressionQuantifier) expression;
            var quantifier = expressionQuantifier.quantifier;
            SFAWrapper sfa = generate(expressionQuantifier.expression);

            if (quantifier.min == 1 && quantifier.max == 1) {
                return sfa;
            }

            var originalInitialState = sfa.getSFA().getInitialState();
            var originalFinalStates = sfa.getSFA().getFinalStates();
            var originalTransitions = sfa.getSFA().getTransitions();

            Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
            Collection<Integer> finalStates = new HashSet<>();

            // Calculate the amount of SFA copies required
            int count = quantifier.max < 0 ? quantifier.min : quantifier.max;

            for (int i = 0; i < count; i++) {
                // Calculate offsets
                int offset = (1 + originalTransitions.size()) * i;
                int nextOffset = (1 + originalTransitions.size()) * (i + 1);

                // Copy original SFA with an offset
                for (var transition : originalTransitions) {
                    if (transition instanceof SFAInputMove) {
                        transitions.add(new SFAInputMove<>(
                                offset + transition.from,
                                offset + transition.to,
                                ((SFAInputMove<CharPred, Character>) transition).guard
                        ));
                    } else if (transition instanceof SFAEpsilon) {
                        transitions.add(new SFAEpsilon<>(
                                offset + transition.from,
                                offset + transition.to
                        ));
                    } else {
                        throw new UnsupportedOperationException(
                                String.format("SFAMove of type \"%s\" is not supported", transition.getClass().getSimpleName())
                        );
                    }
                }

                // Connect final states of this sub-SFA to the initial state of the next sub-SFA
                if (i < count - 1) {
                    for (int state : originalFinalStates) {
                        transitions.add(new SFAEpsilon<>(offset + state, nextOffset + originalInitialState));
                    }
                }
            }

            // Calculate offset of last sub-SFA
            int lastOffset = (1 + originalTransitions.size()) * (count - 1);

            // Connect initial state to the final states of the last sub-SFA
            if (quantifier.min == 0) {
                for (int state : originalFinalStates) {
                    transitions.add(new SFAEpsilon<>(originalInitialState, lastOffset + state));
                }
            }

            // Connect final states of the last sub-SFA to the initial state of the last sub-SFA, i.e. self-transition on the last sub-SFA
            if (quantifier.max < 0) {
                for (int state : originalFinalStates) {
                    transitions.add(new SFAEpsilon<>(lastOffset + state, lastOffset + originalInitialState));
                }
            }

            // Mark the final states of the relevant sub-SFAs as final states for the new SFA
            for (int i = Math.max(quantifier.min, 1); i <= count; i++) {
                int offset = (1 + originalTransitions.size()) * (i - 1);

                for (int state : originalFinalStates) {
                    finalStates.add(offset + state);
                }
            }

            System.out.println("Quantifier: " + quantifier);
            System.out.println("Final: " + finalStates);
            System.out.println(transitions);
            System.out.println();

            return new SFAWrapper(transitions, sfa.getSFA().getInitialState(), finalStates, false);
        } else if (expression instanceof RegexExpressionCharacterClass) {
            var expressionCharacterClass = (RegexExpressionCharacterClass) expression;

            // Generate predicate
            CharPred predicate = null;
            for (CharacterClass.Range range : expressionCharacterClass.characterClass.ranges) {
                var p = new CharPred(range.start, range.end);

                if (predicate == null) {
                    predicate = p;
                } else {
                    predicate = SFAWrapper.ALGEBRA.MkOr(predicate, p);
                }
            }
            if (expressionCharacterClass.characterClass.type == CharacterClass.Type.NEGATED_SET) {
                predicate = SFAWrapper.ALGEBRA.MkNot(predicate);
            }

            // Generate SFA
            Collection<SFAMove<CharPred, Character>> transitions = new LinkedList<>();
            Collection<Integer> finalStates = new HashSet<>();

            transitions.add(new SFAInputMove<>(0, 1, predicate));
            finalStates.add(1);

            return new SFAWrapper(transitions, 0, finalStates, false);
        } else {
            throw new IllegalStateException("Unknown regex expression");
        }
    }

    @Override
    public Collection<Pair<String, SFAWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFAWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("a_z", generate(new Regex(
                new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))))
        )));

        examples.add(Pair.of("a_z_3_to_5", generate(new Regex(
                new RegexExpressionQuantifier(
                        new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))),
                        new Quantifier(3, 5)
                )
        ))));

        examples.add(Pair.of("a_z_or_0_9", generate(new Regex(
                new RegexExpressionOperator(RegexExpressionOperator.Operator.OR, List.of(
                        new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))),
                        new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('0', '9'))))
                )))
        )));

        examples.add(Pair.of("a_z_0_9_3_plus", generate(new Regex(
                new RegexExpressionQuantifier(
                    new RegexExpressionOperator(RegexExpressionOperator.Operator.CONCAT, List.of(
                            new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))),
                            new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('0', '9'))))
                    )),
                    new Quantifier(3, -1)
                )
        ))));

        examples.add(Pair.of("a_z_0_9_2_to_4", generate(new Regex(
                new RegexExpressionQuantifier(
                        new RegexExpressionOperator(RegexExpressionOperator.Operator.CONCAT, List.of(
                                new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))),
                                new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('0', '9'))))
                        )),
                        new Quantifier(2, 4)
                )
        ))));

        examples.add(Pair.of("complex_1", generate(RegexLanguage.parseString("[a-z]+0?"))));
        examples.add(Pair.of("complex_2", generate(RegexLanguage.parseString("(45|_{3})+"))));
        examples.add(Pair.of("complex_3", generate(RegexLanguage.parseString("[a-z]+0?|2(9{1,}|(45|_{3})+)|&"))));
        examples.add(Pair.of("email_1", generate(RegexLanguage.parseString("[a-zA-Z0-9._%\\+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}"))));
        examples.add(Pair.of("email_2", generate(RegexLanguage.parseString("[a-zA-Z0-9._%\\+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,63}"))));

        return examples;
    }

    @Override
    public String format(Regex input) {
        return String.format("regex %s", input.toRegex());
    }
}
