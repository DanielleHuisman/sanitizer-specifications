package io.danielhuisman.sanitizers.language.regex;

import io.danielhuisman.sanitizers.util.Util;

import java.util.List;

public class RegexExpressionOperator extends RegexExpression {

    public enum Operator {
        AND,
        OR
    }

    public Operator operator;
    public List<RegexExpression> expressions;

    public RegexExpressionOperator(Operator operator, List<RegexExpression> expressions) {
        this.operator = operator;
        this.expressions = expressions;
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
