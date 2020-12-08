package io.danielhuisman.sanitizers.sft.string;

import theory.characters.CharPred;

import java.util.Objects;

import static com.google.common.base.Preconditions.checkNotNull;

public class StringConstant implements StringFunc {

    private final String value;

    public StringConstant(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public StringFunc substIn(StringFunc f1) {
        return new StringConstant(checkNotNull(f1).instantiateWith(getValue()));
    }

    @Override
    public CharPred substIn(CharPred p, CharIntervalSolver solver) {
//        return checkNotNull(p).isSatisfiedBy(value) ? StdCharPred.TRUE : StdCharPred.FALSE;
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public String instantiateWith(String s) {
        return getValue();
    }

    @Override
    public String toString() {
        return String.format("x -> %s", getValue());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StringConstant that = (StringConstant) o;
        return Objects.equals(getValue(), that.getValue());
    }

    @Override
    public int hashCode() {
        return Objects.hash(StringConstant.class, getValue());
    }
}
