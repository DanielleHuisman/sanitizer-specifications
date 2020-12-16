package io.danielhuisman.sanitizers.language.ir.types;

import java.util.Arrays;
import java.util.stream.Collectors;

public class TypeTuple extends Type {

    private final Type[] types;

    public TypeTuple(Type... types) {
        super(String.format("tuple[%s]", Arrays.stream(types).map(Type::toString).collect(Collectors.joining(", "))));
        this.types = types;
    }

    public Type[] getTypes() {
        return types;
    }
}
