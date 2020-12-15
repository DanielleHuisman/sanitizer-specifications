package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.Util;
import io.danielhuisman.sanitizers.language.ir.Operator;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class ExpressionOperator extends Expression {

    public Operator operator;
    public List<Expression> operands;

    public ExpressionOperator(Token start, Token end, Operator operator, List<Expression> operands) {
        super(start, end);
        this.operator = operator;
        this.operands = operands;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(operator.toString());
        for (Expression expression : operands) {
            s.append("\n");
            s.append(Util.indent(expression.toString()));
        }
        return s.toString();
    }
}
