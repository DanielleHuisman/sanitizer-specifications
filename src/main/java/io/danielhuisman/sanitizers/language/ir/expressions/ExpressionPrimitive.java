package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.language.ir.Primitive;
import org.antlr.v4.runtime.Token;

public class ExpressionPrimitive<T> extends Expression {

    public Primitive<T> primitive;

    public ExpressionPrimitive(Token start, Token end, Primitive<T> primitive) {
        super(start, end);
        this.primitive = primitive;
    }

    @Override
    public String toString() {
        return primitive.toString();
    }
}
