package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.util.Util;
import io.danielhuisman.sanitizers.language.ir.Memory;
import io.danielhuisman.sanitizers.language.ir.Operator;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.antlr.v4.runtime.Token;
import org.sat4j.specs.TimeoutException;

import java.util.List;
import java.util.stream.Collectors;

public class ExpressionOperator extends Expression {

    public Operator operator;
    public List<Expression> operands;

    public ExpressionOperator(Token start, Token end, Operator operator, List<Expression> operands) {
        super(start, end);
        this.operator = operator;
        this.operands = operands;
    }

    @Override
    public AutomatonWrapper<?, ?> execute(Memory memory) throws TimeoutException {
        var automatons = operands
                .stream()
                .map(Util.wrapper((operand) -> operand.execute(memory)))
                .collect(Collectors.toList());

        Class<?> automatonClass = automatons.get(0).getClass();
        for (var automaton : automatons) {
            if (!automatonClass.isInstance(automaton)) {
                throw new RuntimeException("Automatons are not of the same type");
            }
        }

        switch (operator) {
            case NOT:
                if (automatons.get(0) instanceof SFAWrapper) {
                    return ((SFAWrapper) automatons.get(0)).complement();
                }
                throw new RuntimeException("Automaton is not an SFA");
            case AND:
                if (automatons.get(0) instanceof SFAWrapper) {
                    return ((SFAWrapper) automatons.get(0)).intersectionWith((SFAWrapper) automatons.get(1)).minimize().cleanUp();
                }
                throw new RuntimeException("Automatons are not SFAs");
            case OR:
                if (automatons.get(0) instanceof SFAWrapper) {
                    return ((SFAWrapper) automatons.get(0)).unionWith((SFAWrapper) automatons.get(1)).minimize().cleanUp();
                }
                throw new RuntimeException("Automatons are not SFAs");
            case PLUS:
                if (automatons.get(0) instanceof SFAWrapper) {
                    return ((SFAWrapper) automatons.get(0)).concatenateWith((SFAWrapper) automatons.get(1)).minimize().cleanUp();
                }
                throw new RuntimeException("Automatons are not SFAs");
        }

        return null;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(operator.toString());
        for (Expression expression : operands) {
            s.append("\n");
            s.append(Util.indent(expression.toString()));
        }
        return s.toString();
    }
}
