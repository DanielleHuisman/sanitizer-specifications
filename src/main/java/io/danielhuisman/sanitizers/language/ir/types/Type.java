package io.danielhuisman.sanitizers.language.ir.types;

public class Type {

    public static final Type INTEGER = new Type("integer");
    public static final Type CHARACTER = new Type("character");
    public static final Type STRING = new Type("string");

    private final String name;

    public Type(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
