package io.danielhuisman.sanitizers.generators;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWordList;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorWordList {

    private static final String[] WORDS = {"a", "abc", "</script>", "#abc9ee", "456", ""};

    private String formatInput(List<String> words) {
        if (words.size() == 0) {
            return "[]";
        }
        return String.format("[\"%s\"]", String.join("\", \"", words));
    }

    @Test
    public void testEquals() throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            List<String> match = Arrays.asList(WORDS).subList(0, i);
            SFAWrapper sfa = generator.generate(Pair.of(GeneratorWord.Operator.EQUALS, match));

            for (String input : WORDS) {
                if (match.contains(input) || (match.size() == 0 && input.length() == 0)) {
                    assertTrue(sfa.accepts(input), String.format("Equals %s should accept string \"%s\"", formatInput(match), input));
                } else {
                    assertFalse(sfa.accepts(input), String.format("Equals %s should not accept string \"%s\"", formatInput(match), input));
                }

                assertFalse(sfa.accepts("q" + input), String.format("Equals %s should accept string \"%s\"", formatInput(match), "q" + input));
            }
        }
    }

    @Test
    public void testNotEquals() throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            List<String> match = Arrays.asList(WORDS).subList(0, i);
            SFAWrapper sfa = generator.generate(Pair.of(GeneratorWord.Operator.NOT_EQUALS, match));

            for (String input : WORDS) {
                if (match.contains(input) || (match.size() == 0 && input.length() == 0)) {
                    assertFalse(sfa.accepts(input), String.format("Not equals %s should not accept string \"%s\"", formatInput(match), input));
                } else {
                    assertTrue(sfa.accepts(input), String.format("Not equals %s should accept string \"%s\"", formatInput(match), input));
                }

                assertTrue(sfa.accepts("q" + input), String.format("Not equals %s should not accept string \"%s\"", formatInput(match), "q" + input));
            }
        }
    }

    @Test
    public void testContains () throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            List<String> match = Arrays.asList(WORDS).subList(0, i);
            SFAWrapper sfa = generator.generate(Pair.of(GeneratorWord.Operator.CONTAINS, match));

            for (String input : WORDS) {
                if (match.stream().anyMatch(input::contains) || match.size() == 0) {
                    assertTrue(sfa.accepts(input), String.format("Contains %s should accept string \"%s\"", formatInput(match), input));
                    assertTrue(sfa.accepts("q" + input), String.format("Contains %s should accept string \"%s\"", formatInput(match), "q" + input));
                } else {
                    assertFalse(sfa.accepts(input), String.format("Contains %s should not accept string \"%s\"", formatInput(match), input));
                }
            }
        }
    }

    @Test
    public void testNotContains () throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            List<String> match = Arrays.asList(WORDS).subList(0, i);
            SFAWrapper sfa = generator.generate(Pair.of(GeneratorWord.Operator.NOT_CONTAINS, match));

            for (String input : WORDS) {
                if (match.stream().anyMatch(input::contains) || match.size() == 0) {
                    assertFalse(sfa.accepts(input), String.format("Not contains %s should not accept string \"%s\"", formatInput(match), input));
                } else {
                    assertTrue(sfa.accepts(input), String.format("Not contains %s should accept string \"%s\"", formatInput(match), input));
                    assertTrue(sfa.accepts("q" + input), String.format("Not contains %s should accept string \"%s\"", formatInput(match), "q" + input));
                }
            }
        }
    }
}
