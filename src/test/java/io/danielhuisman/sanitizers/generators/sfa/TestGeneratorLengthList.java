package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorLengthList;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;

import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TestGeneratorLengthList {

    private final GeneratorLengthList generator = new GeneratorLengthList();

    @Test
    public void test() throws TimeoutException {
        test(List.of(), List.of(0, 1, 2, 3, 4), List.of());

        test(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.EQUALS, 1)
                )
        ), List.of(1), List.of(0, 2, 3, 4));

        test(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 1),
                        Pair.of(GeneratorRange.Operator.LESS_THAN, 4)
                )
        ), List.of(2, 3), List.of(0, 1, 4, 5));

        test(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN_OR_EQUALS, 6),
                        Pair.of(GeneratorRange.Operator.LESS_THAN_OR_EQUALS, 4)
                )
        ), List.of(), List.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9));

        test(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 1),
                        Pair.of(GeneratorRange.Operator.LESS_THAN, 4)
                )
        ), List.of(2, 3), List.of(0, 1, 4, 5));

        test(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 1),
                        Pair.of(GeneratorRange.Operator.LESS_THAN, 4)
                ),
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN_OR_EQUALS, 7),
                        Pair.of(GeneratorRange.Operator.LESS_THAN_OR_EQUALS, 10),
                        Pair.of(GeneratorRange.Operator.NOT_EQUALS, 8)
                )
        ), List.of(2, 3, 7, 9, 10), List.of(0, 1, 4, 5, 6, 8, 11));
    }

    private void test(Collection<Collection<Pair<GeneratorRange.Operator, Integer>>> input, Collection<Integer> accept, Collection<Integer> reject)
            throws TimeoutException {
        SFAWrapper sfa = generator.generate(input);

        for (int length : accept) {
            assertTrue(sfa.accepts(StringUtils.repeat('a', length)), String.format("%s should accept string of length %d", generator.format(input), length));
        }

        for (int length : reject) {
            assertFalse(sfa.accepts(StringUtils.repeat('a', length)), String.format("%s should not accept of length %d", generator.format(input), length));
        }
    }

    @Test
    public void testFormat() {
        assertEquals("[[equals 1]]", generator.format(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.EQUALS, 1)
                )
        )));

        assertEquals("[[greater_than 1, less_than 4], [greater_than 8]]", generator.format(List.of(
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 1),
                        Pair.of(GeneratorRange.Operator.LESS_THAN, 4)
                ),
                List.of(
                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 8)
                )
        )));
    }

//    @Test
//    public void testParse() {
//        assertEquals(List.of(
//                List.of(
//                        Pair.of(GeneratorRange.Operator.EQUALS, 1)
//                )
//        ), generator.parse("[[equals 1]]"));
//
//        assertEquals(List.of(
//                List.of(
//                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 1),
//                        Pair.of(GeneratorRange.Operator.LESS_THAN, 4)
//                ),
//                List.of(
//                        Pair.of(GeneratorRange.Operator.GREATER_THAN, 8)
//                )
//        ), generator.parse("[[greater_than 1, less_than 4], [greater_than 8]]"));
//    }
}
