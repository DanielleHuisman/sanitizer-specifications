package io.danielhuisman.sanitizers.sft;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestSFTWrapper {

    @Test
    public void testCompose() {
        assertThrows(RuntimeException.class, () -> SFTWrapper.compose(List.of()));
    }
}
