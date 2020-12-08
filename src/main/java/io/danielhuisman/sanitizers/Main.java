package io.danielhuisman.sanitizers;

import io.danielhuisman.sanitizers.generators.sfa.*;
import io.danielhuisman.sanitizers.generators.sft.SFTGenerator;
import io.danielhuisman.sanitizers.generators.sft.SFTStringGenerator;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import io.danielhuisman.sanitizers.sft.SFTWrapper;
import io.danielhuisman.sanitizers.sft.string.SFTStringWrapper;
import org.apache.commons.lang3.tuple.Pair;
import org.reflections.Reflections;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void generateExamples()  throws ReflectiveOperationException, TimeoutException, IOException {
        // Initialize reflections library
        Reflections reflections = new Reflections("io.danielhuisman.sanitizers.generators");

        // Find SFA generators
        var generatorSFAClasses = reflections.getSubTypesOf(SFAGenerator.class);
        for (var generatorClass : generatorSFAClasses) {
            // Instantiate generator
            var constructor = generatorClass.getConstructor();
            SFAGenerator<?> generator = constructor.newInstance();

            // Generate examples and create dot files
            var examples = generator.generateExamples();

            // Create dot file for each example
            for (var example : examples) {
                String name = example.getLeft();
                SFAWrapper sfa = example.getRight();

                sfa.createDotFile(name, generator.getName() + "/");
            }
        }

        // Find SFT generators
        var generatorSFTClasses = reflections.getSubTypesOf(SFTGenerator.class);
        for (var generatorClass : generatorSFTClasses) {
            // Instantiate generator
            var constructor = generatorClass.getConstructor();
            SFTGenerator<?> generator = constructor.newInstance();

            // Generate examples and create dot files
            var examples = generator.generateExamples();

            // Create dot file for each example
            for (var example : examples) {
                String name = example.getLeft();
                SFTWrapper sfa = example.getRight();

                sfa.createDotFile(name, generator.getName() + "/");
            }
        }

        // Find SFT string generators
        var generatorSFTStringClasses = reflections.getSubTypesOf(SFTStringGenerator.class);
        for (var generatorClass : generatorSFTStringClasses) {
            // Instantiate generator
            var constructor = generatorClass.getConstructor();
            SFTStringGenerator<?> generator = constructor.newInstance();

            // Generate examples and create dot files
            var examples = generator.generateExamples();

            // Create dot file for each example
            for (var example : examples) {
                String name = example.getLeft();
                SFTStringWrapper sfa = example.getRight();

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
                    )
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
                    )
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
