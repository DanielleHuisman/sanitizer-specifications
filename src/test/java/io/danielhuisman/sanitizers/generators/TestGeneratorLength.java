package io.danielhuisman.sanitizers.generators;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorLength;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorLength {

    private static final int SIZE = 10;

    @Test
    public void testEquals() throws TimeoutException {
        GeneratorLength generatorLength = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            SFAWrapper sfa = generatorLength.generate(Pair.of(GeneratorLength.Operator.EQUALS, i));

            for (int j = 0; j <= SIZE; j++) {
                if (j == i) {
                    assertTrue(sfa.accepts(StringUtils.repeat('a', j)), "Equals " + i + " should accept string of length " + j);
                } else {
                    assertFalse(sfa.accepts(StringUtils.repeat('a', j)), "Equals " + i + " should not accept string of length " + j);
                }
            }
        }
    }

    @Test
    public void testNotEquals() throws TimeoutException {
        GeneratorLength generatorLength = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            SFAWrapper sfa = generatorLength.generate(Pair.of(GeneratorLength.Operator.NOT_EQUALS, i));

            for (int j = 0; j <= SIZE; j++) {
                if (j != i) {
                    assertTrue(sfa.accepts(StringUtils.repeat('a', j)), "Not equals " + i + " should accept string of length " + j);
                } else {
                    assertFalse(sfa.accepts(StringUtils.repeat('a', j)), "Not equals " + i + " should not accept string of length " + j);
                }
            }
        }
    }

    @Test
    public void testLessThan() throws TimeoutException {
        GeneratorLength generatorLength = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            SFAWrapper sfa = generatorLength.generate(Pair.of(GeneratorLength.Operator.LESS_THAN, i));

            for (int j = 0; j <= SIZE; j++) {
                if (j < i) {
                    assertTrue(sfa.accepts(StringUtils.repeat('a', j)), "Less than " + i + " should accept string of length " + j);
                } else {
                    assertFalse(sfa.accepts(StringUtils.repeat('a', j)), "Less than " + i + " should not accept string of length " + j);
                }
            }
        }
    }

    @Test
    public void testLessThanOrEquals() throws TimeoutException {
        GeneratorLength generatorLength = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            SFAWrapper sfa = generatorLength.generate(Pair.of(GeneratorLength.Operator.LESS_THAN_OR_EQUALS, i));

            for (int j = 0; j <= SIZE; j++) {
                if (j <= i) {
                    assertTrue(sfa.accepts(StringUtils.repeat('a', j)), "Less than or equals " + i + " should accept string of length " + j);
                } else {
                    assertFalse(sfa.accepts(StringUtils.repeat('a', j)), "Less than or equals " + i + " should not accept string of length " + j);
                }
            }
        }
    }

    @Test
    public void testGreaterThan() throws TimeoutException {
        GeneratorLength generatorLength = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            SFAWrapper sfa = generatorLength.generate(Pair.of(GeneratorLength.Operator.GREATER_THAN, i));

            for (int j = 0; j <= SIZE; j++) {
                if (j > i) {
                    assertTrue(sfa.accepts(StringUtils.repeat('a', j)), "Greater than " + i + " should accept string of length " + j);
                } else {
                    assertFalse(sfa.accepts(StringUtils.repeat('a', j)), "Greater than " + i + " should not accept string of length " + j);
                }
            }
        }
    }

    @Test
    public void testGreaterThanOrEquals() throws TimeoutException {
        GeneratorLength generatorLength = new GeneratorLength();

        for (int i = 0; i < SIZE; i++) {
            SFAWrapper sfa = generatorLength.generate(Pair.of(GeneratorLength.Operator.GREATER_THAN_OR_EQUALS, i));

            for (int j = 0; j <= SIZE; j++) {
                if (j >= i) {
                    assertTrue(sfa.accepts(StringUtils.repeat('a', j)), "Greater than or equals " + i + " should accept string of length " + j);
                } else {
                    assertFalse(sfa.accepts(StringUtils.repeat('a', j)), "Greater than or equals " + i + " should not accept string of length " + j);
                }
            }
        }
    }
}
