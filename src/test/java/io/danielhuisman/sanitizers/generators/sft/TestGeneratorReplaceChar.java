package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Test;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestGeneratorReplaceChar {

    private final GeneratorReplaceChar generator = new GeneratorReplaceChar();

    @Test
    public void test() throws TimeoutException {
        test(List.of(
                Pair.of(new CharPred('a'), "b"),
                Pair.of(new CharPred('b'), "a")
        ), Map.of(
                "", "",
                "a", "b",
                "b", "a",
                "aba", "bab",
                "aaaaa", "bbbbb"
        ));

        test(List.of(
                Pair.of(new CharPred('<'), "&lt;"),
                Pair.of(new CharPred('>'), "&gt;"),
                Pair.of(new CharPred('&'), "&amp;")
        ), Map.of(
                "", "",
                "<script>", "&lt;script&gt;",
                "a & b", "a &amp; b",
                "def", "def",
                "<<&&>>", "&lt;&lt;&amp;&amp;&gt;&gt;",
                "&amp;", "&amp;amp;"
        ));
    }

    private void test(Collection<Pair<CharPred, String>> input, Map<String, String> output) throws TimeoutException {
        SFTWrapper sft = generator.generate(input);

        for (var entry : output.entrySet()) {
            String word = entry.getKey();
            String match = entry.getValue();

            assertTrue(sft.accepts(word), String.format("%s should accept string \"%s\"", generator.format(input), word));

            String result = sft.execute(word);
            assertEquals(match, result);
        }
    }

    @Test
    public void testFormat() {
        assertEquals("[[a] -> \"b\"]", generator.format(
                List.of(
                        Pair.of(new CharPred('a'), "b")
                )
        ));

        assertEquals("[[a-z] -> \"_\", [\\\"] -> \"'\"]", generator.format(
                List.of(
                        Pair.of(new CharPred('a', 'z'), "_"),
                        Pair.of(new CharPred('"'), "'")
                )
        ));
    }
}
