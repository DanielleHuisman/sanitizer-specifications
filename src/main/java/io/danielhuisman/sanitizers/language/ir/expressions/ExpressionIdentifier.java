package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.Memory;
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

    @Override
    public AutomatonWrapper<?, ?> execute(Memory memory) {
        return memory.get(identifier);
    }
}
