package io.danielhuisman.sanitizers.generators.sfa;

import automata.sfa.SFAInputMove;
import automata.sfa.SFAMove;
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
        return generate(input.expression);
    }

    private SFAWrapper generate(RegexExpression expression) throws TimeoutException {
        if (expression instanceof RegexExpressionOperator) {
            var expressionOperator = (RegexExpressionOperator) expression;

            List<SFAWrapper> expressions = expressionOperator.expressions
                    .stream()
                    .map(Util.wrapper(this::generate))
                    .collect(Collectors.toList());

            // TODO: debug weird merges

            return expressionOperator.operator == RegexExpressionOperator.Operator.OR ? SFAWrapper.union(expressions) : SFAWrapper.concatenate(expressions);
        } else if (expression instanceof RegexExpressionQuantifier) {
            var expressionQuantifier = (RegexExpressionQuantifier) expression;
            var quantifier = expressionQuantifier.quantifier;
            SFAWrapper sfa = generate(expressionQuantifier.expression);

            if (quantifier.min == 1 && quantifier.max == 1) {
                return sfa;
            }

            // TODO: modify SFA with quantifier

            // Steps:
            // - Clone original SFA
            // - Remove final states from prev/original SFA
            // - Add transitions between prev/original SFA final states and cloned SFA inputs
            // - Repeat n times to satisfy quantifier and handle open end if necessary

            return null;
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

        examples.add(Pair.of("a_z_plus", generate(new Regex(
                new RegexExpressionQuantifier(
                        new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))),
                        new Quantifier(1, -1)
                )
        ))));

        examples.add(Pair.of("a_z_or_0_9", generate(new Regex(
                new RegexExpressionOperator(RegexExpressionOperator.Operator.OR, List.of(
                        new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('a', 'z')))),
                        new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, List.of(new CharacterClass.Range('0', '9'))))
                )))
        )));

        return examples;
    }

    @Override
    public String format(Regex input) {
        return null;
    }

    @Override
    public Regex parse(String input) {
        return null;
    }
}
