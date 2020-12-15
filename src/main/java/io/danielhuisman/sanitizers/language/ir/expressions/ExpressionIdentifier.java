package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.language.ir.Identifier;
import org.antlr.v4.runtime.Token;

public class ExpressionIdentifier extends Expression {

    public Identifier identifier;

    public ExpressionIdentifier(Token start, Token end, Identifier identifier) {
        super(start, end);
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return identifier.toString();
    }
}
