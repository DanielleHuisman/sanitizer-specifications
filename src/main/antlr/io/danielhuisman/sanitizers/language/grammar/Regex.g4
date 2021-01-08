grammar Regex;

@header {
package io.danielhuisman.sanitizers.language.grammar;
}

DIGIT: [0-9];
CHARACTER: DIGIT | [a-zA-Z!"#$%&'()*+,./:;<=>?@[\\\]^`{|}~ _\-];
SPECIAL_CHARACTER: '.' | '\\w' | '\\W' | '\\d' | '\\D' | '\\s' | '\\S';

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
    | CHARACTER                     # characterClassCharacter
    | DIGIT                         # characterClassCharacter
    ;

range
    : CHARACTER
    | CHARACTER '-' CHARACTER
    ;
