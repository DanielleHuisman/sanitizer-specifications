package io.danielhuisman.sanitizers.generators.sfa;

import io.danielhuisman.sanitizers.generators.Generator;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;
import org.sat4j.specs.TimeoutException;

public abstract class SFAGenerator<I> extends Generator<I, SFAWrapper> {

    public abstract SFAWrapper generate(I input) throws TimeoutException;
}
