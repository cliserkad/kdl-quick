grammar kdl;

@header {
package com.xarql.kdl.antlr4;
}

// skip over comments in lexer
COMMENT: '//' .*? '\n' -> skip;
BLOCK_COMMENT: '/*' .*? '*/' -> skip;

// skip over whitespace
WS : [ \t\r\n]+ -> skip;

// keywords
CLASS: 'class';
CONST: 'const';
RUN: 'run';
METHOD: 'mtd';
FUNCTION: 'fnc';
TRUE: 'true';
FALSE: 'false';
RETURN: 'return';
SEE: 'see';
PKG: 'pkg';
R_IF: 'if';
R_ELSE: 'else';
R_NULL: 'null';
SIZE: 'size';
ASSERT: 'assert';
WHILE: 'while';

// base types
INT: 'int';
BOOLEAN: 'boolean';
STRING: 'string';

// syntax
BODY_OPEN: '{'; // opening bracket
BODY_CLOSE: '}'; // closing bracket
PARAM_OPEN: '('; // opening paren
PARAM_CLOSE: ')'; // closing paren
BRACE_OPEN: '[';
BRACE_CLOSE: ']';
DOT: '.';
SEPARATOR: ',';
STATEMENT_END: ';';
ASSIGN: ':';

// comparator
NOT_EQUAL: '!=';
EQUAL: '=';
REF_NOT_EQUAL: '!?';
REF_EQUAL: '?';
LESS_THAN: '<';
MORE_THAN: '>';
LESS_OR_EQUAL: '<=';
MORE_OR_EQUAL: '>=';

// operators
PLUS: '+';
MINUS: '-';
DIVIDE: '/';
MULTIPLY: '*';
MODULUS: '%';

// appenders
AND: '&';
OR: '|';

DIGIT               : '0' | '1' | '2' | '3' | '4' | '5' | '6' | '7' | '8' | '9';
fragment UPLETTER   : [A-Z];
fragment DNLETTER   : [a-z];
fragment LETTER     : UPLETTER | DNLETTER;
fragment ALPHANUM   : LETTER | DIGIT;
fragment UNDERSCORE : '_';
fragment DNTEXT     : DNLETTER+;

CONSTNAME : UPLETTER (UPLETTER | DIGIT | UNDERSCORE)+;
CLASSNAME : UPLETTER DNLETTER (LETTER | DIGIT)+;
VARNAME   : DNLETTER (LETTER | DIGIT)*;

QUALIFIED_NAME: (DNTEXT '.')+ CLASSNAME;
PKG_NAME: DNTEXT ('.' DNTEXT)*;

ESCAPED_QUOTE : '\\"';

arrayLength: VARNAME SIZE;

// literals
bool: TRUE | FALSE;
number: MINUS? DIGIT+ ('B' | 'H')?;
STRING_LIT: '"' (ESCAPED_QUOTE | ~'"')* '"';
literal: bool | STRING_LIT | number;

statement: methodCall STATEMENT_END | variableDeclaration | variableAssignment | returnStatement | conditional;
block: BODY_OPEN statement* BODY_CLOSE | statement;
statementSet: block | statement;

// conditionals
conditional: r_if | assertion | r_while;
r_if: R_IF PARAM_OPEN condition PARAM_CLOSE statementSet r_else?;
r_else: R_ELSE statementSet;
assertion: ASSERT condition STATEMENT_END;
r_while: WHILE PARAM_OPEN condition PARAM_CLOSE statementSet;

value: methodCall | arrayLength| literal | VARNAME | CONSTNAME | arrayAccess | R_NULL;
operator: PLUS | MINUS | DIVIDE | MULTIPLY | MODULUS;
expression: value (operator value)?;

condition: singleCondition (appender singleCondition)?;
singleCondition: expression (comparator expression)?;
comparator: EQUAL | NOT_EQUAL | REF_EQUAL | REF_NOT_EQUAL | MORE_THAN | LESS_THAN | MORE_OR_EQUAL | LESS_OR_EQUAL;
appender: AND | OR;

variableDeclaration: typedVariable (SEPARATOR VARNAME)* (ASSIGN expression)? STATEMENT_END;
variableAssignment: VARNAME assignment STATEMENT_END;
assignment: (ASSIGN expression) | operatorAssign;
operatorAssign: operator ASSIGN value;
typedVariable: type VARNAME;
arrayAccess: VARNAME BRACE_OPEN expression BRACE_CLOSE;

// method calls
methodCall: VARNAME parameterSet;
parameterSet: PARAM_OPEN (expression (SEPARATOR expression)*)? PARAM_CLOSE;

// method definitions
methodDefinition: methodType type VARNAME parameterDefinition block;
methodType: (METHOD | FUNCTION)?;
parameterDefinition: PARAM_OPEN typedVariable? (SEPARATOR typedVariable)* PARAM_CLOSE;

returnStatement: RETURN expression STATEMENT_END;

type: basetype | CLASSNAME;
basetype: BOOLEAN | INT | STRING;

source: pkg? see* clazz;
pkg: PKG PKG_NAME STATEMENT_END;
see: SEE QUALIFIED_NAME STATEMENT_END;
clazz: CLASS CLASSNAME BODY_OPEN (constant | run | variableDeclaration | methodDefinition)* BODY_CLOSE;
constant: CONST CONSTNAME ASSIGN literal STATEMENT_END;
run: RUN block;