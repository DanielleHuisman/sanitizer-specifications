package io.danielhuisman.sanitizers.generators;

import io.danielhuisman.sanitizers.generators.sfa.SFAGenerator;
import io.danielhuisman.sanitizers.generators.sft.SFTGenerator;
import io.danielhuisman.sanitizers.generators.sft.SFTStringGenerator;
import org.reflections.Reflections;

import java.util.Collection;
import java.util.HashMap;

public class Generators {

    private static final HashMap<String, Generator<?, ?>> GENERATORS = new HashMap<>();

    @SuppressWarnings("unchecked")
    public static void initialize() throws ReflectiveOperationException {
        // Initialize reflections library
        Reflections reflections = new Reflections("io.danielhuisman.sanitizers.generators");

        // Find generators
        Class<?>[] superClasses = {SFAGenerator.class, SFTGenerator.class, SFTStringGenerator.class};

        for (Class<?> superClass : superClasses) {
            var classes = reflections.getSubTypesOf(superClass);
            for (var clazz : classes) {
                Class<? extends Generator<?, ?>> generatorClass = (Class<? extends Generator<?, ?>>) clazz;

                // Instantiate generator
                var constructor = generatorClass.getConstructor();
                Generator<?, ?> generator = constructor.newInstance();

                GENERATORS.put(generator.getName(), generator);
            }
        }
    }

    public static boolean hasGenerators(String name) {
        return GENERATORS.containsKey(name);
    }

    public static Generator<?, ?> getGenerator(String name) {
        return GENERATORS.get(name);
    }

    public static Collection<Generator<?, ?>> getGenerators() {
        return GENERATORS.values();
    }

    public static SFAGenerator<?> getSFAGenerator(String name) {
        var generator = GENERATORS.get(name);
        if (generator instanceof SFAGenerator) {
            return (SFAGenerator<?>) generator;
        }
        throw new RuntimeException(String.format("Generator \"%s\" does not generate SFAs.", generator.getName()));
    }

    public static SFTGenerator<?> getSFTGenerator(String name) {
        var generator = GENERATORS.get(name);
        if (generator instanceof SFTGenerator) {
            return (SFTGenerator<?>) generator;
        }
        throw new RuntimeException(String.format("Generator \"%s\" does not generate SFTs.", generator.getName()));
    }

    public static SFTStringGenerator<?> getSFTStringGenerator(String name) {
        var generator = GENERATORS.get(name);
        if (generator instanceof SFTStringGenerator) {
            return (SFTStringGenerator<?>) generator;
        }
        throw new RuntimeException(String.format("Generator \"%s\" does not generate string SFTs.", generator.getName()));
    }
}
