package io.danielhuisman.sanitizers.language.ir.statements;

import io.danielhuisman.sanitizers.language.ir.Identifier;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringEscapeUtils;

public class StatementAssert extends Statement {

    public enum AssertType {
        ACCEPTS,
        DENIES
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
    public String toString() {
        return "assert " + identifier.toString() + " " + type.name().toLowerCase() + " \"" + StringEscapeUtils.escapeJava(word) + "\"";
    }
}
