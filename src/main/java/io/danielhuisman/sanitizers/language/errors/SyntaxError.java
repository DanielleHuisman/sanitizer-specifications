package io.danielhuisman.sanitizers.language.errors;

import org.antlr.v4.runtime.Token;

public class SyntaxError extends ParseError {

    public SyntaxError(int line, int column, String message) {
        super(line, column, message);
    }

    public SyntaxError(Token start, Token end, String message) {
        super(start, end, message);
    }
}
