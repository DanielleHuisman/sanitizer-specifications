package io.danielhuisman.sanitizers.language.ir.types;

public class TypeList extends Type {

    private final Type type;

    public TypeList(Type type) {
        super(String.format("list[%s]", type.getName()));
        this.type = type;
    }

    public Type getType() {
        return type;
    }
}
