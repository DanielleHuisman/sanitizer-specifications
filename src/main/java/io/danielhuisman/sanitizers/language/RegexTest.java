package io.danielhuisman.sanitizers.language;

public class RegexTest {

    public static void main(String[] args) {
        RegexLanguage.process(RegexLanguage.parseString("[a-z]+b?|a(q{1,}|(ab|c{2,3})+)|z"));
    }
}
