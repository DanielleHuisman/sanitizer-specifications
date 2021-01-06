package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.grammar.RegexLexer;
import io.danielhuisman.sanitizers.language.grammar.RegexParser;
import io.danielhuisman.sanitizers.language.regex.Regex;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

public class RegexLanguage {

    public static Regex parseString(String input) {
        return parse(CharStreams.fromString(input));
    }

    public static Regex parse(CharStream chars) {
        // Create empty regex and error listener
        Regex regex = new Regex(chars.getText(new Interval(0, chars.size())));
        ErrorListener errorListener = new ErrorListener(regex);

        // Create lexer
        RegexLexer lexer = new RegexLexer(chars);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        // Create parser
        RegexParser parser = new RegexParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        // Parse input and use tree listener to generate the program
        ParseTreeWalker walker = new ParseTreeWalker();
        RegexListener listener = new RegexListener(regex);
        walker.walk(listener, parser.expression());

        return regex;
    }

    public static boolean process(Regex regex) {
        System.out.println(regex);

        // Check for errors
        if (regex.getErrors().size() > 0) {
            System.err.println(regex.getFormattedErrors());
            return false;
        }

        return true;
    }
}
