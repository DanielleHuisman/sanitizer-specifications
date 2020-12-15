package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.ir.Program;

import java.io.IOException;

public class LanguageTest {

    public static void main(String[] args) {
        try {
            Program program = Language.parseString("test1 = length \"=\" 10\nprint test1\ntest2 = word \"abc\"");
            System.out.println(program.getFormattedErrors());
            System.out.println(program);

            program = Language.parseFile("examples/test.san");
            System.out.println(program.getFormattedErrors());
            System.out.println(program);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
