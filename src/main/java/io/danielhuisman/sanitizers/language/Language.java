package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.grammar.LanguageLexer;
import io.danielhuisman.sanitizers.language.grammar.LanguageParser;
import io.danielhuisman.sanitizers.language.ir.Program;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.misc.Interval;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.io.IOException;

public class Language {

    public static Program parseString(String input) {
        return parse(CharStreams.fromString(input));
    }

    public static Program parseFile(String path) throws IOException {
        return parse(CharStreams.fromFileName(path));
    }

    public static Program parse(CharStream chars) {
        // Create empty program and error listener
        Program program = new Program(chars.getText(new Interval(0, chars.size())));
        ErrorListener errorListener = new ErrorListener(program);

        // Create lexer
        LanguageLexer lexer = new LanguageLexer(chars);
        lexer.removeErrorListeners();
        lexer.addErrorListener(errorListener);

        // Create parser
        LanguageParser parser = new LanguageParser(new CommonTokenStream(lexer));
        parser.removeErrorListeners();
        parser.addErrorListener(errorListener);

        // Parse input and use tree listener to generate the program
        ParseTreeWalker walker = new ParseTreeWalker();
        LanguageListener listener = new LanguageListener(program);
        walker.walk(listener, parser.program());

        return program;
    }
}
