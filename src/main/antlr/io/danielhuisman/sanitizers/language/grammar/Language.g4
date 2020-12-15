grammar Language;

@header {
package io.danielhuisman.sanitizers.language.grammar;
}

// Fragments
fragment LETTER: [a-zA-Z];
fragment DIGIT: [0-9];
fragment DASH: [-_];

// Primitives
INTEGER: DIGIT+;
CHAR: '\'' ( '\\\'' | '\\'? . ) '\'';
STRING: '"' ( '\\"' | '\\\\' | ~[\\"\r\n] )* '"';
// TODO: check if char and string are valid and UTF-8 capable

// Operators
ASSIGN: '=';
NOT: 'not';
AND: 'and';
OR: 'or';
PLUS: 'plus';

// Tokens
SEMICOLON: ';';
LEFT_PAREN: '(';
RIGHT_PAREN: ')';
LEFT_BRACKET: '[';
RIGHT_BRACKET: ']';
COMMA: ',';
PRINT: 'print';
ACCEPTS: 'accepts';
DENIES: 'denies';

// Identifier
IDENTIFIER: LETTER ((LETTER | DIGIT | DASH)* (LETTER | DIGIT))*;

// Whitespace and comments
WS : [ \t\n\r] -> skip;
COMMENT : '//' ~[\n]+ -> skip;
BLOCK_COMMENT : '/*' .*? '*/' -> skip;

// Helpers
identifier: IDENTIFIER;

// Primitives
primitive
    : INTEGER                                           # primitiveInteger
    | CHAR                                              # primitiveCharacter
    | STRING                                            # primitiveString
    | LEFT_BRACKET (primitive COMMA?)* RIGHT_BRACKET    # primitiveList
    ;

// Program
program: statement* EOF;

// Statements
statement
    : identifier ASSIGN expression      # statementAssignment
    | PRINT identifier                  # statementPrint
    | ACCEPTS identifier STRING         # statementAssert
    | DENIES identifier STRING          # statementAssert
    ;

// Expressions
expression
    : LEFT_PAREN expression RIGHT_PAREN # expressionParens
    | NOT expression                    # expressionOperator
    | expression AND expression         # expressionOperator
    | expression OR expression          # expressionOperator
    | expression PLUS expression        # expressionOperator
    | identifier arguments              # expressionGenerator
    | identifier                        # expressionIdentifier
    ;

arguments: primitive+;
