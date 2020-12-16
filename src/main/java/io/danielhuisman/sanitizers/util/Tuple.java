package io.danielhuisman.sanitizers.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class Tuple<E> {

    private final int size;
    private final ArrayList<E> values;

    public Tuple(int size) {
        this.size = size;
        this.values = new ArrayList<>(size);
    }

    @SafeVarargs
    public Tuple(int size, E... values) {
        this(size);

        if (values.length != size) {
            throw new IllegalArgumentException(String.format("Tuple requires %d values, but %d were provided.", size, values.length));
        }

        for (int i = 0; i < size; i++) {
            this.values.set(i, values[i]);
        }
    }

    public Tuple(int size, List<E> values) {
        this(size);

        if (values.size() != size) {
            throw new IllegalArgumentException(String.format("Tuple requires %d values, but %d were provided.", size, values.size()));
        }

        this.values.addAll(values);
    }

    public int size() {
        return size;
    }

    public Collection<?> values() {
        return values;
    }

    public E get(int index) {
        return values.get(index);
    }

    public void set(int index, E value) {
        values.set(index, value);
    }

    @Override
    public String toString() {
        return String.format("(%s)", values.stream().map(Object::toString).collect(Collectors.joining(", ")));
    }
}
