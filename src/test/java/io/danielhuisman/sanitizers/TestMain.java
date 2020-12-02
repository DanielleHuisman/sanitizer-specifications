package io.danielhuisman.sanitizers;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class TestMain {

    @Test
    public void test() {
        assertDoesNotThrow(Main::generateExamples);
    }
}
