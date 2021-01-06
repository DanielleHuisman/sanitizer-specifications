package io.danielhuisman.sanitizers.language;

import io.danielhuisman.sanitizers.language.regex.Regex;

public class RegexTest {

    public static void main(String[] args) {
        String input = "[a-z]+b?|a(q{1,}|(ab|c{2,3})+)|z";
        Regex regex = RegexLanguage.parseString(input);
        RegexLanguage.process(regex);
        System.out.println("input:  " + input);
        System.out.println("output: " + regex.toRegex());
    }
}
