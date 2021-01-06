package io.danielhuisman.sanitizers.language.regex;

import io.danielhuisman.sanitizers.util.Util;

public class RegexExpressionQuantifier extends RegexExpression {

    public RegexExpression expression;
    public Quantifier quantifier;

    public RegexExpressionQuantifier(RegexExpression expression, Quantifier quantifier) {
        this.expression = expression;
        this.quantifier = quantifier;
    }

    @Override
    public String toRegex() {
        if (!(expression instanceof RegexExpressionCharacterClass)) {
            return "(" + expression.toRegex() + ")" + quantifier.toRegex();
        }
        return expression.toRegex()  + quantifier.toRegex();
    }

    @Override
    public String toString() {
        return "quantifier\n" +
                Util.indent(expression.toString()) + "\n" +
                Util.indent(quantifier.toString());
    }
}
