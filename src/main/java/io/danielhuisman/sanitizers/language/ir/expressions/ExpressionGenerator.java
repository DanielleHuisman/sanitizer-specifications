package io.danielhuisman.sanitizers.language.ir.expressions;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;
import io.danielhuisman.sanitizers.generators.Generator;
import io.danielhuisman.sanitizers.generators.Generators;
import io.danielhuisman.sanitizers.language.ir.Identifier;
import io.danielhuisman.sanitizers.language.ir.Memory;
import io.danielhuisman.sanitizers.language.ir.Primitive;
import io.danielhuisman.sanitizers.util.ICoercibleEnum;
import io.danielhuisman.sanitizers.util.Util;
import org.antlr.v4.runtime.Token;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;
import org.sat4j.specs.TimeoutException;
import theory.characters.CharPred;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.stream.Collectors;

public class ExpressionGenerator extends Expression {

    public Identifier identifier;
    public List<Primitive<?>> arguments;

    public ExpressionGenerator(Token start, Token end, Identifier identifier, List<Primitive<?>> arguments) {
        super(start, end);
        this.identifier = identifier;
        this.arguments = arguments;
    }

    @Override
    @SuppressWarnings("unchecked")
    public AutomatonWrapper<?, ?> execute(Memory memory) throws TimeoutException {
        if (!Generators.hasGenerators(identifier.getName())) {
            throw new RuntimeException(String.format("Generator \"%s\" does not exist", identifier.getName()));
        }

        var generator = (Generator<Object, AutomatonWrapper<?, ?>>) Generators.getGenerator(identifier.getName());

        System.out.println(arguments.get(0).getClass());

        Object input;
        if (arguments.size() == 1) {
            input = arguments.get(0).convert();
        } else {
            throw new NotImplementedException("Arguments of sizes other than 1 are not implemented.");
        }

        System.out.println(generator.getName());
        System.out.println(arguments);

        System.out.println("before: " + input.toString());

        for (Method method : generator.getClass().getMethods()) {
            if (method.getName().equals("generate")) {
                var parameterTypes = method.getGenericParameterTypes();
                if (parameterTypes.length != 1 || parameterTypes[0] == Object.class) {
                    continue;
                }

                if (parameterTypes[0] instanceof ParameterizedType) {
                    input = coerce(input, parameterTypes[0]);
                }

                break;
            }
        }

        System.out.println("after: " + input.toString());

        return generator.generate(input);
    }

    public Object coerce(Object input, Type type) {
        // Coerce parameterized types (pair, triple, list)
        if (type instanceof ParameterizedType) {
            var arguments = ((ParameterizedType) type).getActualTypeArguments();

            if (input instanceof Pair) {
                var pair = (Pair<?, ?>) input;

                return Pair.of(coerce(pair.getLeft(), arguments[0]), coerce(pair.getRight(), arguments[1]));
            } else if (input instanceof Triple) {
                var triple = (Triple<?, ?, ?>) input;

                return Triple.of(coerce(triple.getLeft(), arguments[0]), coerce(triple.getMiddle(), arguments[1]), coerce(triple.getRight(), arguments[2]));
            } else if (input instanceof List) {
                var list = (List<?>) input;

                return list.stream().map((value) -> coerce(value, arguments[0])).collect(Collectors.toList());
            }
        }

        // Find class for this type
        Class<?> typeClass;
        try {
            typeClass = Class.forName(type.getTypeName());
        } catch (ClassNotFoundException e) {
            return input;
        }

        // Coerce enums
        if (input instanceof String) {
            var inputString = (String) input;

            try {
                // Check if the type is an enum
                if (Enum.class.isAssignableFrom(typeClass)) {
                    boolean isCoercible = ICoercibleEnum.class.isAssignableFrom(typeClass);

                    // Loop over enum values
                    for (Field field : typeClass.getDeclaredFields()) {
                        if (field.getType() == typeClass) {
                            Object value = field.get(null);

                            if (field.getName().equalsIgnoreCase(inputString)) {
                                // Replace string with enum value
                                input = value;
                                break;
                            }

                            if (isCoercible) {
                                var coercible = (ICoercibleEnum) value;
                                if (coercible.getAlias().equalsIgnoreCase(inputString)) {
                                    // Replace string with enum value
                                    input = value;
                                    break;
                                }
                            }
                        }
                    }
                }
            } catch (IllegalAccessException e) {
                System.err.println("This error is a warning:");
                e.printStackTrace();
            }
        }

        // Coerce CharPred
        if (typeClass == CharPred.class) {
            if (input instanceof Character) {
                return new CharPred((Character) input);
            } else if (input instanceof Pair) {
                var pair = (Pair<?, ?>) input;

                return new CharPred((Character) pair.getLeft(), (Character) pair.getRight());
            }
        }

        return input;
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("generator\n");
        s.append(Util.indent(identifier.toString()));
        for (Primitive<?> primitive : arguments) {
            s.append("\n");
            s.append(Util.indent(primitive.toString()));
        }
        return s.toString();
    }
}
