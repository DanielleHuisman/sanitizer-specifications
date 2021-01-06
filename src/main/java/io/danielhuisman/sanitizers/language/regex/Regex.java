package io.danielhuisman.sanitizers.language.regex;

import io.danielhuisman.sanitizers.language.SourceContainer;

public class Regex extends SourceContainer {

    public RegexExpression expression;

    public Regex(String source) {
        super(source);
    }

    @Override
    public String toString() {
        return expression.toString();
    }
}
