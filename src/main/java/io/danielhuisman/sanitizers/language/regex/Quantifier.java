package io.danielhuisman.sanitizers.language.regex;

public class Quantifier {

    public int min;
    public int max;

    public Quantifier(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public String toRegex() {
        if (min == 0 && max == 1) {
            return "?";
        } else if (min == 0 && max == -1) {
            return "*";
        } else if (min == 1 && max == -1) {
            return "+";
        } else if (min == max) {
            return "{" + min + "}";
        } else if (max == -1) {
            return "{" + min + ",}";
        } else {
            return "{" + min + "," + max + "}";
        }
    }

    @Override
    public String toString() {
        return max >= 0 ? String.format("{%d, %d}", min, max) : String.format("{%d,}", min);
    }
}
