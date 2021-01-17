package io.danielhuisman.sanitizers.language.regex;

import io.danielhuisman.sanitizers.util.Util;

import java.util.List;
import java.util.stream.Collectors;

public class CharacterClass {

    public enum Type {
        CHARACTER,
        SET,
        NEGATED_SET
    }

    public static class Range {

        public char start;
        public char end;

        public Range(char start, char end) {
            this.start = start;
            this.end = end;
        }

        public String toRegex() {
            return start == end ? String.format("%c", start) : String.format("%c-%c", start, end);
        }

        @Override
        public String toString() {
            return start == end ? String.format("%c", start) : String.format("%c-%c", start, end);
        }
    }

    public static final List<Range> RANGES_DOT = List.of(new Range('\u0000', '\uffff'));
    public static final List<Range> RANGES_WORD = List.of(new Range('A', 'Z'), new Range('a', 'z'), new Range('0', '9'), new Range('_', '_'));
    public static final List<Range> RANGES_DIGIT = List.of(new Range('0', '9'));
    public static final List<Range> RANGES_WHITESPACE = List.of(
            new Range('\t', '\t'),          // Tab
            new Range('\n', '\n'),          // Line feed
            new Range('\u000b', '\u000b'),  // Vertical tab
            new Range('\f', '\f'),          // Form feed
            new Range('\r', '\r'),          // Carriage return
            new Range('\u001c', '\u001f'),  // File, group, record, unit separator
            new Range(' ', ' '),            // Space
            new Range('\u00a0', '\u00a0'),  // Non-breaking space
            new Range('\u2007', '\u2007'),  // Figure space
            new Range('\u202F', '\u202F')   // Narrow non-breaking space
    );

    public Type type;
    public List<Range> ranges;

    public CharacterClass(char character) {
        this.type = Type.CHARACTER;
        this.ranges = List.of(new Range(character, character));
    }

    public CharacterClass(String specialCharacter) {
        if (specialCharacter.equals(".")) {
            this.type = Type.SET;
            this.ranges = RANGES_DOT;
        } else {
            this.type = Character.isUpperCase(specialCharacter.charAt(1)) ? Type.NEGATED_SET : Type.SET;

            switch (specialCharacter.toLowerCase().charAt(1)) {
                case 'w':
                    this.ranges = RANGES_WORD;
                case 'd':
                    this.ranges = RANGES_DIGIT;
                case 's':
                    this.ranges = RANGES_WHITESPACE;
                default: {
                    throw new IllegalStateException(String.format("Unknown special character \"%s\"", specialCharacter));
                }
            }
        }
    }

    public CharacterClass(Type type, List<Range> ranges) {
        this.type = type;
        this.ranges = ranges;
    }

    public String toRegex() {
        switch (type) {
            case CHARACTER:
                return ranges.get(0).toRegex();
            case SET:
                return String.format("[%s]", ranges
                        .stream()
                        .map(Range::toRegex)
                        .collect(Collectors.joining()));
            case NEGATED_SET:
                return String.format("[^%s]", ranges
                        .stream()
                        .map(Range::toRegex)
                        .collect(Collectors.joining()));
        }
        throw new IllegalStateException("Unknown character class type");
    }

    @Override
    public String toString() {
        return type.name().toLowerCase() + (ranges.size() == 1 ?
                " " + ranges.get(0).toString() :
                ranges.stream()
                        .map((range) -> "\n" + Util.indent(range.toString()))
                        .collect(Collectors.joining())
        );
    }
}
