package io.danielhuisman.sanitizers.sft.string;

import theory.characters.CharPred;

import static com.google.common.base.Preconditions.checkNotNull;

public class StringIdentity implements StringFunc {

    @Override
    public StringFunc substIn(StringFunc f1) {
        if (checkNotNull(f1) instanceof StringConstant) {
            return f1;
        }
        return new StringIdentity();
    }

    @Override
    public CharPred substIn(CharPred p, CharIntervalSolver solver) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public String instantiateWith(String s) {
        return s;
    }

    @Override
    public String toString() {
        return "x";
    }
}
