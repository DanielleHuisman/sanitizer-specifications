package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorConstant {

    private static final int SIZE = 10;

    private final GeneratorConstant generator = new GeneratorConstant();

    @Test
    public void test() throws TimeoutException {
        for (String match : new String[]{"", "test", "\"qed\""}) {
            SFTWrapper sft = generator.generate(match);

            for (int i = 0; i < SIZE; i++) {
                for (int j = 0; j < 256; j++) {
                    String input = StringUtils.repeat((char) j, 1 + i);

                    assertTrue(sft.accepts(input), String.format("%s should accept string \"%s\"", generator.format(match), input));
                    assertEquals(match, sft.execute(input), String.format("%s should output \"%s\" for \"%s\"", generator.format(match), match, input));
                }
            }
        }
    }

    @Test
    public void testFormat() {
        assertEquals("constant \"\"", generator.format(""));
        assertEquals("constant \"test\"", generator.format("test"));
        assertEquals("constant \"\\\"\"", generator.format("\""));
    }
}
