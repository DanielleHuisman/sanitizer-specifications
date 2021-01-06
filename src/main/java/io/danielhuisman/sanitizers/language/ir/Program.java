package io.danielhuisman.sanitizers.language.ir;

import io.danielhuisman.sanitizers.language.SourceContainer;
import io.danielhuisman.sanitizers.language.ir.statements.Statement;
import io.danielhuisman.sanitizers.util.Util;
import org.sat4j.specs.TimeoutException;

import java.util.List;

public class Program extends SourceContainer {

    public List<Statement> statements;

    public Program(String source) {
        super(source);
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
