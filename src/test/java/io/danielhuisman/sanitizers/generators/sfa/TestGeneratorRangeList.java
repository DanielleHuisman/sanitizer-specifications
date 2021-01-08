package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneratorRangeList {

    private final GeneratorRangeList generator = new GeneratorRangeList();

    @Test
    public void test() throws TimeoutException {
        test(List.of(
                Pair.of(new CharPred('A', 'Z'), 3)
        ), List.of("ABC", "ZYX"), List.of("abc", "a", "", "AbC", "343"));

        test(List.of(
                Pair.of(new CharPred('A', 'Z'), 3),
                Pair.of(new CharPred('-'), 1),
                Pair.of(new CharPred('0', '9'), 1)
        ), List.of("ABC-5", "ZYX-3"), List.of("abc", "a", "", "AbC9", "343", "abc-1", "QCE--1"));
    }

    private void test(Collection<Pair<CharPred, Integer>> input, Collection<String> accept, Collection<String> reject)
            throws TimeoutException {
        SFAWrapper sfa = generator.generate(input);

        for (String word : accept) {
            assertTrue(sfa.accepts(word), String.format("%s should accept string \"%s\"", generator.format(input), word));
        }

        for (String word : reject) {
            assertFalse(sfa.accepts(word), String.format("%s should not accept \"%s\"", generator.format(input), word));
        }
    }

    @Test
    public void testFormat() {
        assertEquals("[[A-Z] x10]", generator.format(
                List.of(
                        Pair.of(new CharPred('A', 'Z'), 10)
                )
        ));

        assertEquals("[[A-Z] x3, [\\--\\\\] x1, [0-9] x2]", generator.format(
                List.of(
                        Pair.of(new CharPred('A', 'Z'), 3),
                        Pair.of(new CharPred('-', '\\'), 1),
                        Pair.of(new CharPred('0', '9'), 2)
                )
        ));
    }
}
