package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.grammar.RegexBaseListener;
import io.danielhuisman.sanitizers.language.regex.Regex;
import org.antlr.v4.runtime.ParserRuleContext;
import org.antlr.v4.runtime.tree.ParseTree;
import org.antlr.v4.runtime.tree.ParseTreeProperty;

public class RegexListener extends RegexBaseListener {

    private final Regex regex;
    private final ParseTreeProperty<Object> result;

    private String depth = "";

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
        System.out.println(depth + ctx.getClass().getSimpleName() + " " + ctx.getText());
        depth += "    ";
    }

    @Override
    public void exitEveryRule(ParserRuleContext ctx) {
        depth = depth.substring(0, depth.length() - 4);
    }
}
