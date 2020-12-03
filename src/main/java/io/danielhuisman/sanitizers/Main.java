package io.danielhuisman.sanitizers;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorLength;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWord;
import io.danielhuisman.sanitizers.generators.sfa.GeneratorWordList;
import io.danielhuisman.sanitizers.generators.sfa.SFAGenerator;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
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
        var generatorClasses = reflections.getSubTypesOf(SFAGenerator.class);
        for (var generatorClass : generatorClasses) {
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
                            generatorLength.generate(Pair.of(GeneratorLength.Operator.GREATER_THAN, 2)),
                            generatorLength.generate(Pair.of(GeneratorLength.Operator.LESS_THAN_OR_EQUALS, 10))
                    )
            );
            sfa.createDotFile("not_contains_abc_gt_2_lte_10", "combined/");

            String[] words = {"a", "abc", "abcabc", "abacbc", "esefesf", "usheufihsueihfuhseuf"};
            for (String word : words) {
                System.out.println(word + ": " + sfa.accepts(word));
            }
        } catch (TimeoutException | IOException e) {
            e.printStackTrace();
        }
    }
}
