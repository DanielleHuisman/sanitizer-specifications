package io.danielhuisman.sanitizers.generators;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorLength;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneratorLength {

    private static final int SIZE = 10;

    @Test
    public void testEquals() throws TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            var match = Pair.of(GeneratorLength.Operator.EQUALS, i);
            SFAWrapper sfa = generator.generate(match);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j == i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string of length %d", generator.format(match), j));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string of length %d", generator.format(match), j));
                }
            }
        }
    }

    @Test
    public void testNotEquals() throws TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            var match = Pair.of(GeneratorLength.Operator.NOT_EQUALS, i);
            SFAWrapper sfa = generator.generate(match);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j != i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string of length %d", generator.format(match), j));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string of length %d", generator.format(match), j));
                }
            }
        }
    }

    @Test
    public void testLessThan() throws TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            var match = Pair.of(GeneratorLength.Operator.LESS_THAN, i);
            SFAWrapper sfa = generator.generate(match);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j < i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string of length %d", generator.format(match), j));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string of length %d", generator.format(match), j));
                }
            }
        }
    }

    @Test
    public void testLessThanOrEquals() throws TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            var match = Pair.of(GeneratorLength.Operator.LESS_THAN_OR_EQUALS, i);
            SFAWrapper sfa = generator.generate(match);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j <= i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string of length %d", generator.format(match), j));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string of length %d", generator.format(match), j));
                }
            }
        }
    }

    @Test
    public void testGreaterThan() throws TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            var match = Pair.of(GeneratorLength.Operator.GREATER_THAN, i);
            SFAWrapper sfa = generator.generate(match);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j > i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string of length %d", generator.format(match), j));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string of length %d", generator.format(match), j));
                }
            }
        }
    }

    @Test
    public void testGreaterThanOrEquals() throws TimeoutException {
        GeneratorLength generator = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            var match = Pair.of(GeneratorLength.Operator.GREATER_THAN_OR_EQUALS, i);
            SFAWrapper sfa = generator.generate(match);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);

                if (j >= i) {
                    assertTrue(sfa.accepts(input), String.format("%s should accept string of length %d", generator.format(match), j));
                } else {
                    assertFalse(sfa.accepts(input), String.format("%s should not accept string of length %d", generator.format(match), j));
                }
            }
        }
    }

    @Test
    public void testFormat() {
        GeneratorLength generator = new GeneratorLength();

        assertEquals("equals 3", generator.format(Pair.of(GeneratorLength.Operator.EQUALS, 3)));
        assertEquals("less_than 10", generator.format(Pair.of(GeneratorLength.Operator.LESS_THAN, 10)));
    }

    @Test
    public void testParse() {
        GeneratorLength generator = new GeneratorLength();

        assertEquals(Pair.of(GeneratorLength.Operator.EQUALS, 3), generator.parse("equals 3"));
        assertEquals(Pair.of(GeneratorLength.Operator.LESS_THAN, 10), generator.parse("less_than 10"));
    }
}
