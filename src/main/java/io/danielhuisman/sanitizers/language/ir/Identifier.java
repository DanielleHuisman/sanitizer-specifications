package io.danielhuisman.sanitizers.language.ir;

public class Identifier {

    private final String name;

    public Identifier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "identifier " + name;
    }
}
