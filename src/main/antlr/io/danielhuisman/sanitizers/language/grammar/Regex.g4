grammar Regex;

@header {
package io.danielhuisman.sanitizers.language.grammar;
}

// Grammar inspired by a possible BNF for Perl Regular Expression
// https://web.archive.org/web/20090129224504/http://faqts.com/knowledge_base/view.phtml/aid/25718/fid/200

INTEGER: [0-9]+;

METACHARACTER
    : '?'
    | '*'
    | '*?'
    | '+'
    | '+?'
    | '^'
    | '$'
    ;

SPECIAL_CHARACTER
    : '\\t'
    | '\\n'
    | '\\r'
    | '\\f'
    | '\\d'
    | '\\D'
    | '\\s'
    | '\\S'
    | '\\w'
    | '\\W'
    ;

CHARACTER: [0-9a-zA-Z!"#$%&'()*+,-./:;<=>?@[\\\]^_`{|}~];

expression: term ('|' term)*;

term: factor+;

factor: atom metacharacter?;

atom
    : character
//    | specialCharacter
    | '.'
    | '(' expression ')'
    | '[' characterClass ']'
    | '[' '^' characterClass ']'
    | '{' INTEGER '}'
    | '{' INTEGER ',' '}'
    | '{' INTEGER ',' INTEGER '}'
    ;

characterClass: characterRange+;

characterRange
    : character
    | character '-' character
    ;

character
    : CHARACTER
//    | '\\' CHARACTER
    ;

//specialCharacter: SPECIAL_CHARACTER;

metacharacter: METACHARACTER;



