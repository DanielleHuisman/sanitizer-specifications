package io.danielhuisman.sanitizers.generators;

import org.apache.commons.lang3.tuple.Pair;
import org.sat4j.specs.TimeoutException;

import java.util.Collection;

public abstract class Generator<I, O> {

    public abstract String getName();

    public abstract O generate(I input) throws TimeoutException;

    public abstract Collection<Pair<String, O>> generateExamples() throws TimeoutException;
}
