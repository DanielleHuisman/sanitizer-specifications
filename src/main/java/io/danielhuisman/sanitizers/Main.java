package io.danielhuisman.sanitizers;

import io.danielhuisman.sanitizers.generators.sfa.SFAGenerator;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.reflections.Reflections;
import org.sat4j.specs.TimeoutException;

import java.io.IOException;

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
    }
}
