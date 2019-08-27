package chap2.programming;

%% 

%implements Lexer
%function nextToken
%type java_cup.runtime.Symbol
%char

%{
private void newline() {
  errorMsg.newline(yychar);
}

private void err(int pos, String s) {
  errorMsg.error(pos,s);
}

private void err(String s) {
  err(yychar,s);
}

private java_cup.runtime.Symbol tok(int kind, Object value) {
    return new java_cup.runtime.Symbol(kind, yychar, yychar+yylength(), value);
}

private ErrorMsg errorMsg;

Yylex(java.io.InputStream s, ErrorMsg e) {
  this(s);
  errorMsg=e;
}

%}

%eofval{
	{
	 return tok(sym.EOF, null);
        }
%eofval}       


%%
" "	{}
\r\n	{newline();}
","	{return tok(sym.COMMA, null);}
"let"	{return tok(sym.LET, null);}
"type"	{return tok(sym.TYPE, null);}
"array"	{return tok(sym.ARRAY, null);}
"of"	{return tok(sym.OF, null);}
"in"	{return tok(sym.IN, null);}
"int"	{return tok(sym.INT, null);}
"end"	{return tok(sym.END, null);}
"var"	{return tok(sym.VAR, null);}
":"	{return tok(sym.COLON, null);}

