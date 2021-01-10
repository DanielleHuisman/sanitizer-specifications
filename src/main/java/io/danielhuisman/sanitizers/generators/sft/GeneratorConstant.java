package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import io.danielhuisman.sanitizers.util.Util;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharFunc;
import theory.characters.CharPred;
import transducers.sft.SFTInputMove;
import transducers.sft.SFTMove;

import java.util.*;

public class GeneratorConstant extends SFTGenerator<String> {

    @Override
    public String getName() {
        return "constant";
    }

    @Override
    public SFTWrapper generate(String input) throws TimeoutException {
        List<SFTMove<CharPred, CharFunc, Character>> transitions = new LinkedList<>();
        Map<Integer, Set<List<Character>>> finalStatesAndTails = new HashMap<>();

        // Add transition with constant output
        transitions.add(new SFTInputMove<>(0, 1, SFTWrapper.ALGEBRA.True(), Util.toCharFuncList(input)));

        // Add true transition to self without output
        transitions.add(new SFTInputMove<>(1, 1, SFTWrapper.ALGEBRA.True(), List.of()));

        // Mark last state as final
        finalStatesAndTails.put(1, new HashSet<>());

        return new SFTWrapper(transitions, 0, finalStatesAndTails);
    }

    @Override
    public Collection<Pair<String, SFTWrapper>> generateExamples() throws TimeoutException {
        List<Pair<String, SFTWrapper>> examples = new LinkedList<>();

        examples.add(Pair.of("constant_cheese", generate("cheese")));
        examples.add(Pair.of("constant_empty", generate("")));

        return examples;
    }

    @Override
    public String format(String input) {
        return String.format("constant \"%s\"", StringEscapeUtils.escapeJava(input));
    }
}
