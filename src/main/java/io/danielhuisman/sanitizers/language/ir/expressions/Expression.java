package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.language.ir.Node;
import org.antlr.v4.runtime.Token;

public abstract class Expression extends Node<AutomatonWrapper<?, ?>> {

    public Expression(Token start, Token end) {
        super(start, end);
    }
}
