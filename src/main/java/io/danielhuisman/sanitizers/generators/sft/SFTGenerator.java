package io.danielhuisman.sanitizers.generators.sft;

import io.danielhuisman.sanitizers.generators.Generator;
import io.danielhuisman.sanitizers.sft.SFTWrapper;
import org.sat4j.specs.TimeoutException;

public abstract class SFTGenerator<I> extends Generator<I, SFTWrapper> {

    public abstract SFTWrapper generate(I input) throws TimeoutException;
}
