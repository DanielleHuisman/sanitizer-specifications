package io.danielhuisman.sanitizers.sft.string;

import theory.characters.CharPred;

public interface StringFunc {

    StringFunc substIn(StringFunc f1);

    CharPred substIn(CharPred p, CharIntervalSolver charIntervalSolver);

    String instantiateWith(String s);
}
