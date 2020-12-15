package io.danielhuisman.sanitizers.language.ir.statements;

import io.danielhuisman.sanitizers.language.ir.Identifier;
import org.antlr.v4.runtime.Token;

public class StatementPrint extends Statement {

    public Identifier identifier;

    public StatementPrint(Token start, Token end, Identifier identifier) {
        super(start, end);
        this.identifier = identifier;
    }

    @Override
    public String toString() {
        return "print " + identifier.toString();
    }
}
