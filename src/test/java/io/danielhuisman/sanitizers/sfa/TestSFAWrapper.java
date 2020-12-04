package io.danielhuisman.sanitizers.sfa;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSFAWrapper {

    @Test
    public void testConcatenate() {
        assertThrows(RuntimeException.class, () -> SFAWrapper.concatenate(List.of()));
    }

    @Test
    public void testUnion() {
        assertThrows(RuntimeException.class, () -> SFAWrapper.union(List.of()));
    }

    @Test
    public void testIntersection() {
        assertThrows(RuntimeException.class, () -> SFAWrapper.intersection(List.of()));
    }
}
