package io.danielhuisman.sanitizers.language.ir.statements;

import io.danielhuisman.sanitizers.Util;
import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.expressions.Expression;
import org.antlr.v4.runtime.Token;

public class StatementAssignment extends Statement {

    public Identifier identifier;
    public Expression expression;

    public StatementAssignment(Token start, Token end, Identifier identifier, Expression expression) {
        super(start, end);
        this.identifier = identifier;
        this.expression = expression;
    }

    @Override
    public String toString() {
        return "assignment\n" +
                Util.indent(identifier.toString()) + "\n" +
                Util.indent(expression.toString());
    }
}
