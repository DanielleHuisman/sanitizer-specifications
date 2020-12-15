package io.danielhuisman.sanitizers.language.ir.statements;

import io.danielhuisman.sanitizers.language.ir.Node;
import org.antlr.v4.runtime.Token;

public abstract class Statement extends Node<Void> {

    public Statement(Token start, Token end) {
        super(start, end);
    }
}
