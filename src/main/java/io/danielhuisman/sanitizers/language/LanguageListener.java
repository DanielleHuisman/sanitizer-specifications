package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.errors.SyntaxError;
import io.danielhuisman.sanitizers.language.grammar.LanguageBaseListener;
import io.danielhuisman.sanitizers.language.grammar.LanguageParser;
import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.Operator;
import io.danielhuisman.sanitizers.language.ir.Primitive;
import io.danielhuisman.sanitizers.language.ir.Program;
import io.danielhuisman.sanitizers.language.ir.expressions.Expression;
import io.danielhuisman.sanitizers.language.ir.expressions.ExpressionGenerator;
import io.danielhuisman.sanitizers.language.ir.expressions.ExpressionIdentifier;
import io.danielhuisman.sanitizers.language.ir.expressions.ExpressionOperator;
import io.danielhuisman.sanitizers.language.ir.statements.Statement;
import io.danielhuisman.sanitizers.language.ir.statements.StatementAssert;
import io.danielhuisman.sanitizers.language.ir.statements.StatementAssignment;
import io.danielhuisman.sanitizers.language.ir.statements.StatementPrint;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.antlr.v4.runtime.tree.TerminalNode;
import org.antlr.v4.runtime.tree.TerminalNodeImpl;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class LanguageListener extends LanguageBaseListener {

    private final Program program;
    private final ParseTreeProperty<Object> result;

    public LanguageListener(Program program) {
        this.program = program;
        this.result = new ParseTreeProperty<>();
    }

    @SuppressWarnings("unchecked")
    private <T> T get(ParseTree node) {
        return (T) result.get(node);
    }

    private <T> void set(ParseTree node, T value) {
        result.put(node, value);
    }

    @Override
    public void exitProgram(LanguageParser.ProgramContext ctx) {
        program.statements = ctx.statement()
                .stream()
                .map((statement) -> (Statement) get(statement))
                .collect(Collectors.toList());
    }

    @Override
    public void exitStatementAssignment(LanguageParser.StatementAssignmentContext ctx) {
        set(ctx, new StatementAssignment(
                ctx.start,
                ctx.stop,
                get(ctx.identifier()),
                get(ctx.expression())
        ));
    }

    @Override
    public void exitStatementPrint(LanguageParser.StatementPrintContext ctx) {
        set(ctx, new StatementPrint(
                ctx.start,
                ctx.stop,
                get(ctx.identifier())
        ));
    }

    @Override
    public void exitStatementAssert(LanguageParser.StatementAssertContext ctx) {
        set(ctx, new StatementAssert(
                ctx.start,
                ctx.stop,
                get(ctx.identifier()),
                ctx.children.get(0).getText().equals("accepts") ? StatementAssert.AssertType.ACCEPTS : StatementAssert.AssertType.DENIES,
                ctx.STRING() == null ? null : ctx.STRING().getText().substring(1, ctx.STRING().getText().length() - 1)
        ));
    }

    @Override
    public void exitExpressionParens(LanguageParser.ExpressionParensContext ctx) {
        set(ctx, get(ctx.expression()));
    }

    @Override
    public void exitExpressionOperator(LanguageParser.ExpressionOperatorContext ctx) {
        var operators = ctx.children
                .stream()
                .filter((child) -> child instanceof TerminalNodeImpl)
                .map((child) -> ((TerminalNode) child).getText())
                .collect(Collectors.toList());
        var operands = ctx.children
                .stream()
                .filter((child) -> child instanceof LanguageParser.ExpressionContext)
                .map((child) -> (Expression) get(child))
                .collect(Collectors.toList());

        if (operators.size() != 1) {
            throw new IllegalStateException("Expression has no or more than one operator");
        }

        var operator = Arrays.stream(Operator.values()).filter((op) -> op.name().equalsIgnoreCase(operators.get(0))).findFirst();
        if (operator.isEmpty()) {
            throw new IllegalStateException(String.format("Unknown operator \"%s\"", operators.get(0)));
        }

        if (operator.get().getOperands() != operands.size()) {
            program.addError(new SyntaxError(ctx.start, ctx.stop, String.format(
                    "Operator %s has incorrect amount of operands, expected %d, but received %d",
                    operator.get().getName(),
                    operator.get().getOperands(),
                    operands.size()
            )));
        }

        set(ctx, new ExpressionOperator(
                ctx.start,
                ctx.stop,
                operator.get(),
                operands
        ));
    }


    @Override
    public void exitExpressionGenerator(LanguageParser.ExpressionGeneratorContext ctx) {
        Identifier identifier = get(ctx.identifier());
        List<Primitive<?>> arguments = ctx.primitive()
                .stream()
                .map((primitive) -> (Primitive<?>) get(primitive))
                .collect(Collectors.toList());

        set(ctx, new ExpressionGenerator(
                ctx.start,
                ctx.stop,
                identifier,
                arguments
        ));
    }

    @Override
    public void exitExpressionIdentifier(LanguageParser.ExpressionIdentifierContext ctx) {
        Identifier identifier = get(ctx.identifier());

        set(ctx, new ExpressionIdentifier(
                ctx.start,
                ctx.stop,
                identifier
        ));
    }

    @Override
    public void exitIdentifier(LanguageParser.IdentifierContext ctx) {
        if (ctx.IDENTIFIER() != null) {
            set(ctx, new Identifier(ctx.IDENTIFIER().getText()));
        }
    }

    @Override
    public void exitPrimitiveInteger(LanguageParser.PrimitiveIntegerContext ctx) {
        set(ctx, new Primitive<>(Integer.class, Integer.parseInt(ctx.getText())));
    }

    @Override
    public void exitPrimitiveCharacter(LanguageParser.PrimitiveCharacterContext ctx) {
        String s = ctx.getText().substring(1, ctx.getText().length() - 1);
        set(ctx, new Primitive<>(Character.class, StringEscapeUtils.unescapeJava(s).charAt(0)));
    }

    @Override
    public void exitPrimitiveString(LanguageParser.PrimitiveStringContext ctx) {
        String s = ctx.getText().substring(1, ctx.getText().length() - 1);
        set(ctx, new Primitive<>(String.class, StringEscapeUtils.unescapeJava(s)));
    }

    @Override
    public void exitPrimitiveList(LanguageParser.PrimitiveListContext ctx) {
        set(ctx, new Primitive<>(
                List.class,
                ctx.primitive()
                    .stream()
                    .map((primitive) -> (Primitive<?>) get(primitive))
                    .collect(Collectors.toList())
        ));
    }
}
