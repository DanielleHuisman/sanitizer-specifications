package io.danielhuisman.sanitizers.sft.string;

import theory.BooleanAlgebraSubst;
import theory.characters.CharPred;
import theory.intervals.UnaryCharIntervalSolver;
import utilities.Pair;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkNotNull;

public class CharIntervalSolver extends BooleanAlgebraSubst<CharPred, StringFunc, String> {

    private final UnaryCharIntervalSolver solver = new UnaryCharIntervalSolver();

    @Override
    public StringFunc MkSubstFuncFunc(StringFunc f1, StringFunc f2) {
        return checkNotNull(f1).substIn(checkNotNull(f2));
    }

    @Override
    public String MkSubstFuncConst(StringFunc f, String s) {
        return checkNotNull(f).instantiateWith(s);
    }

    @Override
    public CharPred MkSubstFuncPred(StringFunc stringFunc, CharPred charPred) {
        return checkNotNull(stringFunc).substIn(charPred, this);
    }

    @Override
    public StringFunc MkFuncConst(String s) {
        return new StringConstant(checkNotNull(s));
    }

    @Override
    public boolean CheckGuardedEquality(CharPred charPred, StringFunc f1, StringFunc f2) {
        throw new UnsupportedOperationException("Not implemented.");
//        CharPred f1IsNotEqualToF2;
//        if (checkNotNull(f1) instanceof StringConstant && checkNotNull(f2) instanceof StringConstant) {
//            f1IsNotEqualToF2 = ((StringConstant) f1).getValue().equals(((StringConstant) f2).getValue()) ? False() : True();
//        } else {
//            // TODO: I guess this is right?
//            f1IsNotEqualToF2 = True();
//        }
//        return !IsSatisfiable(MkAnd(charPred, f1IsNotEqualToF2));
    }

    @Override
    public CharPred getRestrictedOutput(CharPred charPred, StringFunc stringFunc) {
        throw new UnsupportedOperationException("Not implemented.");
    }

    @Override
    public CharPred MkAtom(String s) {
        return new CharPred(s.charAt(0));
    }

    @Override
    public CharPred MkNot(CharPred charPred) {
        return solver.MkNot(charPred);
    }

    @Override
    public CharPred MkOr(Collection<CharPred> collection) {
        return solver.MkOr(collection);
    }

    @Override
    public CharPred MkOr(CharPred p1, CharPred p2) {
        return solver.MkOr(p1, p2);
    }

    @Override
    public CharPred MkAnd(Collection<CharPred> collection) {
        return solver.MkAnd(collection);
    }

    @Override
    public CharPred MkAnd(CharPred p1, CharPred p2) {
        return solver.MkAnd(p1, p2);
    }

    @Override
    public CharPred True() {
        return solver.True();
    }

    @Override
    public CharPred False() {
        return solver.False();
    }

    @Override
    public boolean AreEquivalent(CharPred p1, CharPred p2) {
        return solver.AreEquivalent(p1, p2);
    }

    @Override
    public boolean IsSatisfiable(CharPred charPred) {
        return solver.IsSatisfiable(charPred);
    }

    @Override
    public boolean HasModel(CharPred charPred, String s) {
        return solver.HasModel(charPred, s.charAt(0));
    }

    @Override
    public boolean HasModel(CharPred charPred, String s, String s1) {
        return solver.HasModel(charPred, s.charAt(0), s1.charAt(0));
    }

    @Override
    public String generateWitness(CharPred charPred) {
        return Character.toString(solver.generateWitness(charPred));
    }

    @Override
    public Pair<String, String> generateWitnesses(CharPred charPred) {
        Pair<Character, Character> result = solver.generateWitnesses(charPred);
        return new Pair<>(Character.toString(result.getFirst()), Character.toString(result.getSecond()));
    }
}
