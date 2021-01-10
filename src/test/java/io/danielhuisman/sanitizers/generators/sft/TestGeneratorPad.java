package io.danielhuisman.sanitizers.generators.sft;

import org.apache.commons.lang3.tuple.Triple;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TestGeneratorPad {

    private static final int SIZE = 10;

    private final GeneratorPad generator = new GeneratorPad();

    @Test
    public void test() throws TimeoutException {
        // TODO
    }

    @Test
    public void testFormat() {
        assertEquals("pad 3 \"a\" no trim", generator.format(Triple.of(3, "a", false)));
        assertEquals("pad 10 \"\\\"b\" trim", generator.format(Triple.of(10, "\"b", true)));
    }
}
