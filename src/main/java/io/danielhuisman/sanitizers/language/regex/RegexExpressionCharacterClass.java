package io.danielhuisman.sanitizers.language.regex;

public class RegexExpressionCharacterClass<T> extends RegexExpression {

    public enum CharacterClassType {
        CHARACTER,
        SET,
        SPECIAL_CHARACTER
    }

    public CharacterClassType type;
    public T value;

    public RegexExpressionCharacterClass(CharacterClassType type, T value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public String toString() {
        return "character class " + type.name() + " " + value.toString();
    }
}
