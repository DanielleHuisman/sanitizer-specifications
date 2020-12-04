package io.danielhuisman.sanitizers.generators;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorWord {

    private static final int SIZE = 10;

    @Test
    public void testEquals() throws TimeoutException {
        GeneratorWord generator = new GeneratorWord();

        for (int i = 0; i < SIZE; i++) {
            String match = StringUtils.repeat('a', i);
            var matchInput = Pair.of(GeneratorWord.Operator.EQUALS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j == i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string \"%s\"", generator.format(matchInput), input));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), input));
                }
            }
        }
    }

    @Test
    public void testNotEquals() throws TimeoutException {
        GeneratorWord generator = new GeneratorWord();

        for (int i = 0; i < SIZE; i++) {
            String match = StringUtils.repeat('a', i);
            var matchInput = Pair.of(GeneratorWord.Operator.NOT_EQUALS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j != i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string \"%s\"", generator.format(matchInput), input));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(matchInput), input));
                }
            }
        }
    }

    @Test
    public void testContains() throws TimeoutException {
        GeneratorWord generator = new GeneratorWord();

        for (int i = 0; i < SIZE; i++) {
            String match = StringUtils.repeat('a', i);
            var matchInput = Pair.of(GeneratorWord.Operator.CONTAINS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (int j = 0; j <= SIZE; j++) {
                String prefix = StringUtils.repeat('b', j);
                String postfix = StringUtils.repeat('b', Math.max(0, j - 1));
                String inputValid = prefix + match + postfix;
                String inputInvalid = prefix + "q" + postfix;

                assertTrue(sfa.accepts(inputValid), String.format("%s should accept string \"%s\"", generator.format(matchInput), inputValid));

                // Handle contains empty string
                if (match.length() == 0) {
                    assertTrue(sfa.accepts(inputInvalid), String.format("%s should accept string \"%s\"", generator.format(matchInput), inputInvalid));
                } else {
                    assertFalse(sfa.accepts(inputInvalid), String.format("%s should not accept string \"%s\"", generator.format(matchInput), inputInvalid));
                }
            }
        }
    }

    @Test
    public void testNotContains() throws TimeoutException {
        GeneratorWord generator = new GeneratorWord();

        for (int i = 0; i < SIZE; i++) {
            String match = StringUtils.repeat('a', i);
            var matchInput = Pair.of(GeneratorWord.Operator.NOT_CONTAINS, match);
            SFAWrapper sfa = generator.generate(matchInput);

            for (int j = 0; j <= SIZE; j++) {
                String prefix = StringUtils.repeat('b', j);
                String postfix = StringUtils.repeat('b', Math.max(0, j - 1));
                String inputValid = prefix + match + postfix;
                String inputInvalid = prefix + "q" + postfix;

                assertFalse(sfa.accepts(inputValid), String.format("%s should not accept string \"%s\"", generator.format(matchInput), inputValid));

                // Handle not contains empty string
                if (match.length() == 0) {
                    assertFalse(sfa.accepts(inputInvalid), String.format("%s should not accept string \"%s\"", generator.format(matchInput), inputInvalid));
                } else {
                    assertTrue(sfa.accepts(inputInvalid), String.format("%s should accept string \"%s\"", generator.format(matchInput), inputInvalid));
                }
            }
        }
    }
}
