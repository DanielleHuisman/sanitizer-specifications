package io.danielhuisman.sanitizers.language.regex;

import io.danielhuisman.sanitizers.language.SourceContainer;

public class Regex extends SourceContainer {

    public RegexExpression expression;

    public Regex(String source) {
        super(source);
    }

    public String toRegex() {
        String regex = expression.toRegex();
        if (regex.startsWith("(") && regex.endsWith(")")) {
            return regex.substring(1, regex.length() - 1);
        }
        return regex;
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
