grammar Regex;

@header {
package io.danielhuisman.sanitizers.language.grammar;
}

INTEGER: [0-9]+;
CHARACTER: [0-9a-zA-Z!"#$%&'()*+,-./:;<=>?@[\\\]^_`{|}~ ];
SPECIAL_CHARACTER: '.' | '\\w' | '\\W' | '\\d' | '\\D' | '\\s' | '\\S';

regex: expression EOF;

expression
    : characterClass                    # expressionCharacterClass
    | expression expression             # expressionAnd
    | expression quantifier             # expressionQuantifier
    | expression '|' expression          # expressionOr
    | '(' expression ')'                # expressionParens
    ;

quantifier
    : '*'
    | '+'
    | '?'
    | '{' INTEGER '}'
    | '{' INTEGER ',' '}'
    | '{' INTEGER ',' INTEGER '}'
    ;

characterClass
    : set                       # characterClassSet
    | SPECIAL_CHARACTER         # characterClassSpecial
    | CHARACTER                 # characterClassCharacter
    ;

set
    : '[' CHARACTER* ']'
    | '[' '^' CHARACTER* ']'
    ;
