package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.generators.sfa.GeneratorRegex;
import io.danielhuisman.sanitizers.language.regex.Regex;
import io.danielhuisman.sanitizers.sfa.SFAWrapper;

public class RegexTest {

    public static void main(String[] args) {
        try {
            String[] inputs = {
                    "[a-z0-9]",
                    "[a-z]+b?|a(q{1,}|(ab|c{2,3})+)|z",
                    "[A-Z0-9._%\\+\\-]+@[A-Z0-9.\\-]+\\.[A-Z]{2,}"
            };

            for (String input : inputs) {
                Regex regex = RegexLanguage.parseString(input);
                System.out.println("input:  " + input);
                System.out.println("output: " + regex.toRegex());
                System.out.println();
            }

            GeneratorRegex generatorRegex = new GeneratorRegex();
            SFAWrapper sfa = generatorRegex.generate(RegexLanguage.parseString("[a-zA-Z0-9._%\\+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,63}"));
            System.out.println(sfa.accepts("daniel@huisman.me"));
            System.out.println(sfa.accepts("danielhuisman.me"));
            System.out.println(sfa.accepts("da@test"));
            System.out.println(sfa.accepts("da+test@test.nl"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
