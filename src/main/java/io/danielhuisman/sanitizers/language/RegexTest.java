package io.danielhuisman.sanitizers.language;

public class RegexTest {

    public static void main(String[] args) {
        RegexLanguage.process(RegexLanguage.parseString("[a-z]+b?|q{1,}|(abc*)+"));
    }
}
