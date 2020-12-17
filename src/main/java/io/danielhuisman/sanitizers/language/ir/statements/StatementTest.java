package io.danielhuisman.sanitizers.language.ir.statements;

import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.Memory;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.StringEscapeUtils;
import org.sat4j.specs.TimeoutException;

public class StatementTest extends Statement {

    public Identifier identifier;
    public String input;
    public String output;

    public StatementTest(Token start, Token end, Identifier identifier, String input, String output) {
        super(start, end);
        this.identifier = identifier;
        this.input = input;
        this.output = output;
    }

    @Override
    public Void execute(Memory memory) throws TimeoutException {
        if (!memory.has(identifier)) {
            throw new RuntimeException(String.format("Identifier \"%s\" does not exist", identifier.getName()));
        }

        var automaton = memory.get(identifier);

        if (!automaton.accepts(input)) {
            throw new RuntimeException(String.format("Automaton \"%s\" does not accept \"%s\"", identifier.getName(), StringEscapeUtils.escapeJava(input)));
        }

        String automatonOutput = automaton.execute(input);
        if (automatonOutput == null || !automatonOutput.equals(output)) {
            throw new RuntimeException(String.format(
                    "Automaton \"%s\" outputs %s for \"%s\", but expected \"%s\"",
                    identifier.getName(),
                    automatonOutput == null ? "null" : "\"" + StringEscapeUtils.escapeJava(automatonOutput) + "\"",
                    StringEscapeUtils.escapeJava(input),
                    StringEscapeUtils.escapeJava(output)
            ));
        }

        return null;
    }

    @Override
    public String toString() {
        return String.format("test %s \"%s\" -> \"%s\"", identifier.toString(), StringEscapeUtils.escapeJava(input), StringEscapeUtils.escapeJava(output));
    }
}
