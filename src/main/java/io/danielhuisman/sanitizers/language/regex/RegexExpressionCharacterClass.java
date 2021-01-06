package io.danielhuisman.sanitizers.language.regex;

public class RegexExpressionCharacterClass extends RegexExpression {

    public CharacterClass characterClass;

    public RegexExpressionCharacterClass(CharacterClass characterClass) {
        this.characterClass = characterClass;
    }

    @Override
    public String toRegex() {
        return characterClass.toRegex();
    }

    @Override
    public String toString() {
        return characterClass.toString();
    }
}
