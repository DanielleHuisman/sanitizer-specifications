package io.danielhuisman.sanitizers.sft;

import org.sat4j.specs.TimeoutException;
import theory.BooleanAlgebraSubst;
import theory.characters.CharFunc;
import theory.characters.CharPred;
import theory.intervals.UnaryCharIntervalSolver;
import transducers.sft.SFT;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SFTWrapper {

    public static final BooleanAlgebraSubst<CharPred, CharFunc, Character> ALGEBRA = new UnaryCharIntervalSolver();

    private final SFT<CharPred, CharFunc, Character> sft;

    public SFTWrapper(SFT<CharPred, CharFunc, Character> sft) {
        this.sft = sft;
    }

    public SFT<CharPred, CharFunc, Character> getSFT() {
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

    public boolean accepts(List<Character> input) throws TimeoutException {
        return getSFT().accepts(input, ALGEBRA);
    }

    public boolean accepts(String input) throws TimeoutException {
        return accepts(input.chars().mapToObj(c -> (char) c).collect(Collectors.toList()));
    }

    public String execute(List<Character> input) throws TimeoutException {
        return getSFT().outputOn(input, ALGEBRA).stream().map((c) -> Character.toString(c)).collect(Collectors.joining());
    }

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
