package io.danielhuisman.sanitizers.language.regex;

import io.danielhuisman.sanitizers.util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class RegexExpressionOperator extends RegexExpression {

    public enum Operator {
        CONCAT,
        OR
    }

    public Operator operator;
    public List<RegexExpression> expressions;

    public RegexExpressionOperator(Operator operator, List<RegexExpression> expressions) {
        this.operator = operator;
        this.expressions = expressions;
    }

    @Override
    public String toRegex() {
        String regex = expressions
                .stream()
                .map(RegexExpression::toRegex)
                .collect(Collectors.joining(operator == Operator.CONCAT ? "" : "|"));

        return operator == Operator.OR ? String.format("(%s)", regex) : regex;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(operator.name().toLowerCase());
        for (RegexExpression expression : expressions) {
            s.append("\n");
            s.append(Util.indent(expression.toString()));
        }
        return s.toString();
    }
}
