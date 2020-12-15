package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.errors.SyntaxError;
import io.danielhuisman.sanitizers.language.ir.Program;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorListener extends BaseErrorListener {

    private final Program program;

    public ErrorListener(Program program) {
        this.program = program;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        program.addError(new SyntaxError(line, charPositionInLine, msg));
    }
}
