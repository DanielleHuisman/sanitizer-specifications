package io.danielhuisman.sanitizers.language.ir;

import io.danielhuisman.sanitizers.util.Util;
import io.danielhuisman.sanitizers.language.errors.ParseError;
import io.danielhuisman.sanitizers.language.ir.statements.Statement;
import org.sat4j.specs.TimeoutException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Program {

    private final String[] source;
    private final List<ParseError> errors;

    public List<Statement> statements;

    public Program(String source) {
        this.source = source.split("\n");
        this.errors = new ArrayList<>();
    }

    public String[] getSource() {
        return source;
    }

    public String getSourceLine(int line) {
        return line - 1 >= source.length ? "" : source[line - 1];
    }

    public String[] getSourceLines(int lineStart, int lineEnd) {
        return Arrays.copyOfRange(source, lineStart - 1, lineEnd);
    }

    public List<ParseError> getErrors() {
        return errors;
    }

    public String getFormattedErrors() {
        StringBuilder s = new StringBuilder();
        for (ParseError error : errors) {
            s.append(error.getMessage(this));
            s.append("\n\n");
        }
        return s.substring(0, Math.max(0, s.length() - 2));
    }

    public void addError(ParseError error) {
        errors.add(error);
    }

    public void execute() throws TimeoutException {
        Memory memory = new Memory();
        for (Statement statement : statements) {
            statement.execute(memory);
        }
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("program");
        for (Statement statement : statements) {
            s.append("\n");
            s.append(Util.indent(statement.toString()));
        }
        return s.toString();
    }
}
