package io.danielhuisman.sanitizers.language.ir;

import org.antlr.v4.runtime.Token;
import org.sat4j.specs.TimeoutException;

public abstract class Node<T> {

    private final Token start;
    private final Token end;

    public Node(Token start, Token end) {
        this.start = start;
        this.end = end;
    }

    public Token getStart() {
        return start;
    }

    public Token getEnd() {
        return end;
    }

    public abstract T execute(Memory memory) throws TimeoutException;
}
