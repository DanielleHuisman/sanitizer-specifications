package io.danielhuisman.sanitizers.automaton;

import automata.Automaton;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;

public interface AutomatonWrapper<P, S> {

    Automaton<P, S> getAutomaton();

    void createDotFile(String fileName) throws IOException;

    void createDotFile(String fileName, String path) throws IOException;

    boolean accepts(String input) throws TimeoutException;
}
