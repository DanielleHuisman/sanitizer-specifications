package io.danielhuisman.sanitizers;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.generators.Generator;
import io.danielhuisman.sanitizers.generators.Generators;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorLength;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorRange;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWordList;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void generateExamples()  throws ReflectiveOperationException, TimeoutException, IOException {
        // Find generators
        Generators.initialize();
        System.out.printf("Found generators: %s%n", Generators.getGenerators().stream().map(Generator::getName).collect(Collectors.joining(", ")));

        // Loop over all generators
        for (Generator<?, ?> generator : Generators.getGenerators()) {
            // Generate examples and create dot files
            var examples = generator.generateExamples();

            // Create dot file for each example
            for (var example : examples) {
                String name = example.getLeft();
                AutomatonWrapper<?, ?> sfa = example.getRight();

                sfa.createDotFile(name, generator.getName() + "/");
            }
        }
    }

    public static void main(String[] args) {
        try {
            // Generate examples and their dot files
            generateExamples();
        } catch (ReflectiveOperationException | TimeoutException | IOException e) {
            e.printStackTrace();
        }

        // Test with combinations
        try {
            GeneratorWordList generatorWordList = new GeneratorWordList();
            GeneratorLength generatorLength = new GeneratorLength();

            SFAWrapper sfa = SFAWrapper.intersection(
                    List.of(
                            generatorWordList.generate(Pair.of(GeneratorWord.Operator.NOT_CONTAINS, List.of("abc"))),
                            generatorLength.generate(Pair.of(GeneratorRange.Operator.GREATER_THAN, 2)),
                            generatorLength.generate(Pair.of(GeneratorRange.Operator.LESS_THAN_OR_EQUALS, 10))
                    ),
                    true
            );
            sfa.createDotFile("not_contains_abc_gt_2_lte_10", "combined/");

            String[] words = {"a", "abc", "abcabc", "abacbc", "esefesf", "usheufihsueihfuhseuf"};
            System.out.println("not_contains_abc_gt_2_lte_10");
            for (String word : words) {
                System.out.println(word + ": " + sfa.accepts(word));
            }
            System.out.println();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }

        // Test with concatenation
        try {
            GeneratorWord generatorWord = new GeneratorWord();

            SFAWrapper sfa = SFAWrapper.concatenate(
                    List.of(
                            generatorWord.generate(Pair.of(GeneratorWord.Operator.EQUALS, "abc")),
                            generatorWord.generate(Pair.of(GeneratorWord.Operator.EQUALS, "def"))
                    ),
                    true
            );
            sfa.createDotFile("abc_def", "concat/");

            String[] words = {"a", "abc", "abcdef", "def", "abcde"};
            System.out.println("abc_def");
            for (String word : words) {
                System.out.println(word + ": " + sfa.accepts(word));
            }
            System.out.println();
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
