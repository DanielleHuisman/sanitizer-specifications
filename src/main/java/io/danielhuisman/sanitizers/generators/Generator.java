package io.danielhuisman.sanitizers.generators;

import org.sat4j.specs.TimeoutException;

public abstract class Generator<I, O> {

    public abstract O generate(I input) throws TimeoutException;
}
