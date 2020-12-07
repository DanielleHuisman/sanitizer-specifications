package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneratorTrim {

    private static final int SIZE = 10;

    private final GeneratorTrim generator = new GeneratorTrim();

    @Test
    public void test() throws TimeoutException {
        for (int i = 0; i < SIZE; i++) {
            SFTWrapper sft = generator.generate(i);

            for (int j = 0; j <= SIZE; j++) {
                String input = StringUtils.repeat('a', j);
                String match = StringUtils.repeat('a', Math.min(i, j));

                assertTrue(sft.accepts(input), String.format("%s should accept string of length %d", generator.format(i), j));
                assertEquals(match, sft.execute(input), String.format("%s should output string of length %d", generator.format(i), j));
            }
        }
    }

    @Test
    public void testFormat() {
        assertEquals("trim 3", generator.format(3));
        assertEquals("trim 10", generator.format(10));
    }

    @Test
    public void testParse() {
        assertEquals(3, generator.parse("trim 3"));
        assertEquals(10, generator.parse("trim 10"));
        assertNull(generator.parse("trm"));
        assertNull(generator.parse("trim d"));
    }
}
