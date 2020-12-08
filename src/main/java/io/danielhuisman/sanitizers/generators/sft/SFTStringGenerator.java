package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.generators.Generator;
import io.danielhuisman.sanitizers.sft.string.SFTStringWrapper;
import org.sat4j.specs.TimeoutException;

public abstract class SFTStringGenerator<I> extends Generator<I, SFTStringWrapper> {

    public abstract SFTStringWrapper generate(I input) throws TimeoutException;
}
