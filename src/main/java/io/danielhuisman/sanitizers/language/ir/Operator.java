package io.danielhuisman.sanitizers.language.ir;

public enum Operator {

    NOT(1),
    AND(2),
    OR(2),
    PLUS(2);

    private final int operands;

    Operator(int operands) {
        this.operands = operands;
    }

    public String getName() {
        return name().toLowerCase();
    }

    public int getOperands() {
        return operands;
    }
}
