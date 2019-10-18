set CLASSPATH=.;D:\Workspace\moderncompilerinjava\JLex;D:\Workspace\moderncompilerinjava\java_cup;D:\Workspace\moderncompilerinjava\java_cup\runtime;

cd d:\Workspace\moderncompilerinjava\src\JLex
javac Main.java
cd d:\Workspace\moderncompilerinjava\src\
del /Q chap2\programming\Yylex.java
java JLex.Main chap2\programming\Tiger.lex
move chap2\programming\Tiger.lex.java chap2\programming\Yylex.java
cd d:\Workspace\moderncompilerinjava\src

javac chap2\programming\ErrorMsg.java chap2\programming\ErrorMsg.java chap2\programming\Main.java chap2\programming\sym.java chap2\programming\Yylex.java chap2\programming\Lexer.java
java chap2/programming/Main D:\Workspace\moderncompilerinjava\reference\tiger\testcases\test1.tig
