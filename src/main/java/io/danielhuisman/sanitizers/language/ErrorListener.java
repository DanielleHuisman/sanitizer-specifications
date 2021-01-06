package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.errors.SyntaxError;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;

public class ErrorListener extends BaseErrorListener {

    private final SourceContainer container;

    public ErrorListener(SourceContainer container) {
        this.container = container;
    }

    @Override
    public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
        container.addError(new SyntaxError(line, charPositionInLine, msg));
    }
}
