package io.danielhuisman.sanitizers.language.ir;

import io.danielhuisman.sanitizers.automaton.AutomatonWrapper;

import java.util.HashMap;

public class Memory {

    private final HashMap<String, AutomatonWrapper<?, ?>> automatons;

    public Memory() {
        automatons = new HashMap<>();
    }

    public boolean has(String name) {
        return automatons.containsKey(name);
    }

    public boolean has(Identifier identifier) {
        return has(identifier.getName());
    }

    public AutomatonWrapper<?, ?> get(String name) {
        return automatons.get(name);
    }

    public AutomatonWrapper<?, ?> get(Identifier identifier) {
        return get(identifier.getName());
    }

    public void set(String name, AutomatonWrapper<?, ?> automaton) {
        automatons.put(name, automaton);
    }

    public void set(Identifier identifier, AutomatonWrapper<?, ?> automaton) {
        set(identifier.getName(), automaton);
    }
}
