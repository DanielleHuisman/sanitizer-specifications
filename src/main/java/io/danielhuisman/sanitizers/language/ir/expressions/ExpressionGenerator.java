package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.Util;
import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.Primitive;
import org.antlr.v4.runtime.Token;

import java.util.List;

public class ExpressionGenerator extends Expression {

    public Identifier identifier;
    public List<Primitive<?>> arguments;

    public ExpressionGenerator(Token start, Token end, Identifier identifier, List<Primitive<?>> arguments) {
        super(start, end);
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("generator\n");
        s.append(Util.indent(identifier.toString()));
        for (Primitive<?> primitive : arguments) {
            s.append("\n");
            s.append(Util.indent(primitive.toString()));
        }
        return s.toString();
    }
}
