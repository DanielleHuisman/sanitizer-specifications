package io.danielhuisman.sanitizers;

import io.danielhuisman.sanitizers.util.Util;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TestUtil {

    private int test(int n) throws IllegalStateException {
        if (n == 0) {
            return n;
        }
        throw new IllegalStateException("Illegal state " + n);
    }

    @Test
    public void testWrapper() {
        assertDoesNotThrow(() -> Util.wrapper(this::test).apply(0));
        assertEquals(0, Util.wrapper(this::test).apply(0));
        assertThrows(RuntimeException.class, () -> Util.wrapper(this::test).apply(1));
    }
}
