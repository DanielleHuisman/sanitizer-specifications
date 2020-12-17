package io.danielhuisman.sanitizers.sft;

import automata.Automaton;
import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import org.sat4j.specs.TimeoutException;
import theory.BooleanAlgebraSubst;
import theory.characters.CharFunc;
import theory.characters.CharPred;
import theory.intervals.UnaryCharIntervalSolver;
import transducers.sft.SFT;
import transducers.sft.SFTMove;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SFTWrapper implements AutomatonWrapper<CharPred, Character> {

    public static final BooleanAlgebraSubst<CharPred, CharFunc, Character> ALGEBRA = new UnaryCharIntervalSolver();

    private final SFT<CharPred, CharFunc, Character> sft;

    public SFTWrapper(SFT<CharPred, CharFunc, Character> sft) {
        this.sft = sft;
    }

    public SFTWrapper(Collection<SFTMove<CharPred, CharFunc, Character>> transitions, Integer initialState,
                      Map<Integer, Set<List<Character>>> finalStatesAndTails) throws TimeoutException {
        this.sft = SFT.MkSFT(transitions, initialState, finalStatesAndTails, ALGEBRA);
    }

    @Override
    public Automaton<CharPred, Character> getAutomaton() {
        return sft;
    }

    public SFT<CharPred, CharFunc, Character> getSFT() {
        return sft;
    }

    @Override
    public void createDotFile(String fileName) throws IOException {
        Files.createDirectories(Paths.get("dot"));

        getSFT().createDotFile(fileName, "dot/");
    }

    @Override
    public void createDotFile(String fileName, String path) throws IOException {
        Files.createDirectories(Paths.get("dot/" + path));

        getSFT().createDotFile(fileName, "dot/" + path);
    }

    public boolean accepts(List<Character> input) throws TimeoutException {
        return getSFT().accepts(input, ALGEBRA);
    }

    @Override
    public boolean accepts(String input) throws TimeoutException {
        return accepts(input.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    public String execute(List<Character> input) throws TimeoutException {
        List<Character> output = getSFT().outputOn(input, ALGEBRA);
        if (output == null) {
            return null;
        }
        return output.stream().map((c) -> Character.toString(c)).collect(Collectors.joining());
    }

    @Override
    public String execute(String input) throws TimeoutException {
        return execute(input.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    public SFTWrapper composeWith(SFTWrapper other) throws TimeoutException {
        return new SFTWrapper(getSFT().composeWith(other.getSFT(), ALGEBRA));
    }

    public static SFTWrapper compose(Collection<SFTWrapper> sfts) throws TimeoutException {
        if (sfts.size() == 0) {
            throw new RuntimeException("At least one SFT must be provided.");
        }

        SFTWrapper result = null;
        for (SFTWrapper sft : sfts) {
            if (result == null) {
                result = sft;
            } else {
                result = result.composeWith(sft);
            }
        }
        return result;
    }
}
