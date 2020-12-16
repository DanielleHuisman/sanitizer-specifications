package io.danielhuisman.sanitizers.language.ir.statements;

import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.Memory;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringEscapeUtils;
import org.sat4j.specs.TimeoutException;

public class StatementAssert extends Statement {

    public enum AssertType {
        ACCEPTS,
        REJECTS
    }

    public Identifier identifier;
    public AssertType type;
    public String word;

    public StatementAssert(Token start, Token end, Identifier identifier, AssertType type, String word) {
        super(start, end);
        this.identifier = identifier;
        this.type = type;
        this.word = word;
    }

    @Override
    public Void execute(Memory memory) throws TimeoutException {
        if (!memory.has(identifier)) {
            throw new RuntimeException(String.format("Identifier \"%s\" does not exist", identifier.getName()));
        }

        var automaton = memory.get(identifier);

        if (type == AssertType.ACCEPTS && !automaton.accepts(word)) {
            System.out.println(word + " " + word.length() + " " + StringEscapeUtils.unescapeJava(word).length());
            throw new RuntimeException(String.format("Automaton \"%s\" does not accept \"%s\"", identifier.getName(), StringEscapeUtils.escapeJava(word)));
        } else if (type == AssertType.REJECTS && automaton.accepts(word)) {
            throw new RuntimeException(String.format("Automaton \"%s\" does not deny \"%s\"", identifier.getName(), StringEscapeUtils.escapeJava(word)));
        }

        return null;
    }

    @Override
    public String toString() {
        return "assert " + identifier.toString() + " " + type.name().toLowerCase() + " \"" + StringEscapeUtils.escapeJava(word) + "\"";
    }
}
