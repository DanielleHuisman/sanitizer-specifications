package io.danielhuisman.sanitizers.language.regex;

public class Quantifier {

    public int min;
    public int max;

    public Quantifier(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public String toString() {
        return max >= 0 ? String.format("{%d, %d}", min, max) : String.format("{%d,}", min);
    }
}
