package io.danielhuisman.sanitizers.language.errors;

import io.danielhuisman.sanitizers.Util;
import io.danielhuisman.sanitizers.language.ir.Program;
import org.antlr.v4.runtime.Token;

public class ParseError {

    private final int startLine;
    private final int startColumn;
    private final String message;
    private int endLine;
    private int endColumn;

    public ParseError(int line, int column, String message) {
        this.startLine = line;
        this.startColumn = column;
        this.message = message;
        this.endLine = -1;
        this.endColumn = -1;
    }

    public ParseError(Token start, Token end, String message) {
        this(start.getLine(), start.getCharPositionInLine(), message);
        this.endLine = end.getLine();
        this.endColumn = end.getCharPositionInLine();
    }

    public String getMessage() {
        return String.format("%s: %s (line %d, column %d)", getClass().getSimpleName(), message, startLine, startColumn);
    }

    public String getMessage(Program program) {
        String lines;
        if (endLine >= 0 && startLine != endLine) {
            lines = String.join("\n", program.getSourceLines(startLine, endLine));
        } else {
            lines = program.getSourceLine(startLine);
            lines += "\n" + " ".repeat(startColumn) + "^".repeat(endColumn >= 0 ? endColumn - startColumn + 1 : 1);
        }

        return String.format("%s: %s (line %d, column %d)\n%s", getClass().getSimpleName(), message, startLine, startColumn, Util.indent(lines));
    }

    @Override
    public String toString() {
        return getMessage();
    }
}
