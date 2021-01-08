grammar Regex;

@header {
package io.danielhuisman.sanitizers.language.grammar;
}

DASH: '-';
DIGIT: [0-9];
CHARACTER: [a-zA-Z!"#$%&'()*+,./:;<=>?@[\\\]^`{|}~ _];
ESCAPED_CHARACTER: '\\.' | '\\-' | '\\+' | '\\\\';
SPECIAL_CHARACTER: '.' | '\\w' | '\\W' | '\\d' | '\\D' | '\\s' | '\\S';

character: DIGIT | CHARACTER | ESCAPED_CHARACTER;

regex: expression EOF;

expression
    : characterClass                    # expressionCharacterClass
    | expression quantifier             # expressionQuantifier
    | expression expression             # expressionConcat
    | expression '|' expression         # expressionOr
    | '(' expression ')'                # expressionParens
    ;

quantifier
    : '*'
    | '+'
    | '?'
    | '{' DIGIT+ '}'
    | '{' DIGIT+ ',' '}'
    | '{' DIGIT+ ',' DIGIT+ '}'
    ;

characterClass
    : '[' range* ']'                # characterClassSet
    | '[' '^' range* ']'            # characterClassNegatedSet
    | SPECIAL_CHARACTER             # characterClassSpecial
    | character                     # characterClassCharacter
    ;

range
    : character
    | character DASH character
    ;
