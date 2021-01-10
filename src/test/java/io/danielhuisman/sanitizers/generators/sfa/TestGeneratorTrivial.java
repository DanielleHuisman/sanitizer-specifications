package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneratorTrivial {

    private static final int SIZE = 10;

    private final GeneratorTrivial generator = new GeneratorTrivial();

    @Test
    public void test() throws TimeoutException {
        SFAWrapper sfaAccepting = generator.generate(true);
        SFAWrapper sfaRejecting = generator.generate(false);

        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < 256; j++) {
                String input = StringUtils.repeat((char) j, i);

                assertTrue(sfaAccepting.accepts(input), String.format("%s should accept string \"%s\"", generator.format(true), input));
                assertFalse(sfaRejecting.accepts(input), String.format("%s should not accept string \"%s\"", generator.format(false), input));
            }
        }
    }

    @Test
    public void testFormat() {
        assertEquals("trivial accept", generator.format(true));
        assertEquals("trivial reject", generator.format(false));
    }
}
