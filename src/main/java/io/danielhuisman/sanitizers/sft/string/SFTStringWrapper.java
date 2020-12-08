package io.danielhuisman.sanitizers.sft.string;

import org.sat4j.specs.TimeoutException;
import theory.BooleanAlgebraSubst;
import theory.characters.CharPred;
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

public class SFTStringWrapper {

    public static final BooleanAlgebraSubst<CharPred, StringFunc, String> ALGEBRA = new CharIntervalSolver();

    private final SFT<CharPred, StringFunc, String> sft;

    public SFTStringWrapper(SFT<CharPred, StringFunc, String> sft) {
        this.sft = sft;
    }

    public SFTStringWrapper(Collection<SFTMove<CharPred, StringFunc, String>> transitions, Integer initialState,
                            Map<Integer, Set<List<String>>> finalStatesAndTails) throws TimeoutException {
        this.sft = SFT.MkSFT(transitions, initialState, finalStatesAndTails, ALGEBRA);
    }

    public SFT<CharPred, StringFunc, String> getSFT() {
        return sft;
    }

    public void createDotFile(String fileName) throws IOException {
        Files.createDirectories(Paths.get("dot"));

        getSFT().createDotFile(fileName, "dot/");
    }

    public void createDotFile(String fileName, String path) throws IOException {
        Files.createDirectories(Paths.get("dot/" + path));

        getSFT().createDotFile(fileName, "dot/" + path);
    }

    public boolean accepts(String input) throws TimeoutException {
        return getSFT().accepts(input.chars().mapToObj(Character::toString).collect(Collectors.toList()), ALGEBRA);
    }

    public String execute(String input) throws TimeoutException {
        List<String> output = getSFT().outputOn(input.chars().mapToObj(Character::toString).collect(Collectors.toList()), ALGEBRA);
        if (output == null) {
            return null;
        }
        return String.join("", output);
    }

    public SFTStringWrapper composeWith(SFTStringWrapper other) throws TimeoutException {
        return new SFTStringWrapper(getSFT().composeWith(other.getSFT(), ALGEBRA));
    }

    public static SFTStringWrapper compose(Collection<SFTStringWrapper> sfts) throws TimeoutException {
        if (sfts.size() == 0) {
            throw new RuntimeException("At least one SFT must be provided.");
        }

        SFTStringWrapper result = null;
        for (SFTStringWrapper sft : sfts) {
            if (result == null) {
                result = sft;
            } else {
                result = result.composeWith(sft);
            }
        }
        return result;
    }
}
