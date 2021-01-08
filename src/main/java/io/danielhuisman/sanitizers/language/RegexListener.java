package io.danielhuisman.sanitizers.language;

import com.google.common.base.Strings;
import io.danielhuisman.sanitizers.language.grammar.RegexBaseListener;
import io.danielhuisman.sanitizers.language.grammar.RegexParser;
import io.danielhuisman.sanitizers.language.regex.*;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;
import org.apache.commons.lang3.StringEscapeUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class RegexListener extends RegexBaseListener {

    private final Regex regex;
    private final ParseTreeProperty<Object> result;

    private int depth = 0;

    public RegexListener(Regex regex) {
        this.regex = regex;
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
    public void enterEveryRule(ParserRuleContext ctx) {
        System.out.println(Strings.repeat("|   ", depth) + ctx.getClass().getSimpleName() + " " + ctx.getText());
        depth++;
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        depth--;
    }

    @Override
    public void exitRegex(RegexParser.RegexContext ctx) {
        regex.expression = get(ctx.expression());
    }

    @Override
    public void exitExpressionCharacterClass(RegexParser.ExpressionCharacterClassContext ctx) {
        set(ctx, get(ctx.characterClass()));
    }

    @Override
    public void exitExpressionQuantifier(RegexParser.ExpressionQuantifierContext ctx) {
        set(ctx, new RegexExpressionQuantifier(get(ctx.expression()), get(ctx.quantifier())));
    }

    public void exitExpressionOperator(ParserRuleContext ctx, RegexExpressionOperator.Operator operator, List<RegexExpression> expressions) {
        if (expressions.size() != 2) {
            throw new IllegalStateException("Operator has less or more than 2 expressions");
        }

        List<RegexExpression> list = new LinkedList<>();

        for (RegexExpression expression : expressions) {
            if (expression instanceof RegexExpressionOperator) {
                var expressionOperator = (RegexExpressionOperator) expression;

                if (expressionOperator.operator == operator) {
                    list.addAll(expressionOperator.expressions);
                } else {
                    list.add(expressionOperator);
                }
            } else {
                list.add(expression);
            }
        }

        set(ctx, new RegexExpressionOperator(operator, list));
    }

    @Override
    public void exitExpressionConcat(RegexParser.ExpressionConcatContext ctx) {
        List<RegexExpression> expressions = ctx.expression()
                .stream()
                .map((expression) -> (RegexExpression) get(expression))
                .collect(Collectors.toList());

        exitExpressionOperator(ctx, RegexExpressionOperator.Operator.CONCAT, expressions);
    }

    @Override
    public void exitExpressionOr(RegexParser.ExpressionOrContext ctx) {
        List<RegexExpression> expressions = ctx.expression()
                .stream()
                .map((expression) -> (RegexExpression) get(expression))
                .collect(Collectors.toList());

        exitExpressionOperator(ctx, RegexExpressionOperator.Operator.OR, expressions);
    }

    @Override
    public void exitExpressionParens(RegexParser.ExpressionParensContext ctx) {
        set(ctx, get(ctx.expression()));
    }

    @Override
    public void exitQuantifier(RegexParser.QuantifierContext ctx) {
        String text = ctx.getText();
        int min;
        int max;

        if (text.equals("*")) {
            min = 0;
            max = -1;
        } else if (text.equals("+")) {
            min = 1;
            max = -1;
        } else if (text.equals("?")) {
            min = 0;
            max = 1;
        } else if (!text.contains(",")) {
            min = Integer.parseInt(text.substring(1, text.length() - 1));
            max = min;
        } else {
            min = Integer.parseInt(text.substring(1, text.indexOf(",")));
            if (text.contains(",}")) {
                max = -1;
            } else {
                max = Integer.parseInt(text.substring(text.indexOf(",") + 1, text.length() - 1));
            }
        }

        set(ctx, new Quantifier(min, max));
    }

    @Override
    public void exitCharacterClassCharacter(RegexParser.CharacterClassCharacterContext ctx) {
        String text = ctx.getText();

        if (text.startsWith("\\")) {
            text = text.substring(1);
        }

        System.out.println(text);

        set(ctx, new RegexExpressionCharacterClass(new CharacterClass(text.charAt(0))));
    }

    @Override
    public void exitCharacterClassSpecial(RegexParser.CharacterClassSpecialContext ctx) {
        set(ctx, new RegexExpressionCharacterClass(new CharacterClass(ctx.SPECIAL_CHARACTER().getText())));
    }

    @Override
    public void exitCharacterClassSet(RegexParser.CharacterClassSetContext ctx) {
        List<CharacterClass.Range> ranges = ctx.range()
                .stream()
                .map((range) -> (CharacterClass.Range) get(range))
                .collect(Collectors.toList());

        set(ctx, new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.SET, ranges)));
    }

    @Override
    public void exitCharacterClassNegatedSet(RegexParser.CharacterClassNegatedSetContext ctx) {
        List<CharacterClass.Range> ranges = ctx.range()
                .stream()
                .map((range) -> (CharacterClass.Range) get(range))
                .collect(Collectors.toList());

        set(ctx, new RegexExpressionCharacterClass(new CharacterClass(CharacterClass.Type.NEGATED_SET, ranges)));
    }

    @Override
    public void exitRange(RegexParser.RangeContext ctx) {
        var chars = ctx.character();

        char start = StringEscapeUtils.unescapeJava(chars.get(0).getText()).charAt(0);
        char end = chars.size() == 1 ? start : StringEscapeUtils.unescapeJava(chars.get(1).getText()).charAt(0);

        set(ctx, new CharacterClass.Range(start, end));
    }
}
