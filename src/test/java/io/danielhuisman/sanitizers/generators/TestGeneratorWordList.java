package io.danielhuisman.sanitizers.generators;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWordList;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorWordList {

    private static final String[] WORDS = {"a", "abc", "</script>", "#abc9ee", "456", ""};

    @Test
    public void testEquals() throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            Collection<String> match = Arrays.asList(WORDS).subList(0, i);
            var matchInput = Pair.of(GeneratorWord.Operator.EQUALS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (String input : WORDS) {
                if (match.contains(input) || (match.size() == 0 && input.length() == 0)) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string \"%s\"", generator.format(matchInput), input));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), input));
                }

                assertFalse(sfa.accepts("q" + input), String.format("%s should accept string \"%s\"", generator.format(matchInput), "q" + input));
            }
        }
    }

    @Test
    public void testNotEquals() throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            Collection<String> match = Arrays.asList(WORDS).subList(0, i);
            var matchInput = Pair.of(GeneratorWord.Operator.NOT_EQUALS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (String input : WORDS) {
                if (match.contains(input) || (match.size() == 0 && input.length() == 0)) {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), input));
                } else {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string \"%s\"", generator.format(matchInput), input));
                }

                assertTrue(sfa.accepts("q" + input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), "q" + input));
            }
        }
    }

    @Test
    public void testContains () throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            Collection<String> match = Arrays.asList(WORDS).subList(0, i);
            var matchInput = Pair.of(GeneratorWord.Operator.CONTAINS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (String input : WORDS) {
                if (match.stream().anyMatch(input::contains) || match.size() == 0) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string \"%s\"", generator.format(matchInput), input));
                    assertTrue(sfa.accepts("q" + input), String.format("%s should accept string \"%s\"", generator.format(matchInput), "q" + input));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), input));
                }
            }
        }
    }

    @Test
    public void testNotContains () throws TimeoutException {
        GeneratorWordList generator = new GeneratorWordList();

        for (int i = 0; i <= WORDS.length; i++) {
            Collection<String> match = Arrays.asList(WORDS).subList(0, i);
            var matchInput = Pair.of(GeneratorWord.Operator.NOT_CONTAINS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (String input : WORDS) {
                if (match.stream().anyMatch(input::contains) || match.size() == 0) {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), input));
                } else {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string \"%s\"", generator.format(matchInput), input));
                    assertTrue(sfa.accepts("q" + input), String.format("%s should accept string \"%s\"", generator.format(matchInput), "q" + input));
                }
            }
        }
    }
}
