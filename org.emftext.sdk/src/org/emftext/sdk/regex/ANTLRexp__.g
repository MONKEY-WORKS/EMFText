lexer grammar ANTLRexp;

@members {
  public java.util.List<RecognitionException> lexerExceptions  = new java.util.ArrayList<RecognitionException>();

  public void reportError(RecognitionException e) {
     lexerExceptions.add(e);
	}
	
 
}
@header {
package org.emftext.sdk.codegen.regex; 

}

T__10 : '|' ;
T__11 : '?' ;
T__12 : '*' ;
T__13 : '+' ;
T__14 : '^' ;
T__15 : '!' ;
T__16 : '..' ;
T__17 : '.' ;
T__18 : '(' ;
T__19 : ')' ;
T__20 : '~' ;

// $ANTLR src "./src/org/emftext/sdk/codegen/regex/ANTLRexp.g" 244
CHAR_LITERAL	:	'\'' LITERAL_CHAR '\'' ;

// $ANTLR src "./src/org/emftext/sdk/codegen/regex/ANTLRexp.g" 246
STRING_LITERAL :	'\'' LITERAL_CHAR LITERAL_CHAR* '\'';

// $ANTLR src "./src/org/emftext/sdk/codegen/regex/ANTLRexp.g" 248
fragment LITERAL_CHAR :	ESC  |	~( '\'' | '\\' );

// $ANTLR src "./src/org/emftext/sdk/codegen/regex/ANTLRexp.g" 250
fragment ESC	:	'\\' (	'n' 	|	'r'  |	't'   |	'b' |	'f' |	'"'|	'\''	|	'\\' |	'>' |	'u' XDIGIT XDIGIT XDIGIT XDIGIT |	. );

// $ANTLR src "./src/org/emftext/sdk/codegen/regex/ANTLRexp.g" 252
fragment XDIGIT : '0' .. '9' |	'a' .. 'f' 	|	'A' .. 'F';

// $ANTLR src "./src/org/emftext/sdk/codegen/regex/ANTLRexp.g" 254
WS	:	(	' ' | '\t'|	'\r'? '\n' )+ {$channel=HIDDEN;};