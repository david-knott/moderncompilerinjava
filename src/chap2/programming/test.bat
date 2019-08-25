echo "test"
cd d:\Workspace\moderncompilerinjava\src\JLex
javac Main.java
cd d:\Workspace\moderncompilerinjava\src\
del /Q chap2\programming\Yylex.java
java JLex.Main chap2\programming\Tiger.lex
move chap2\programming\Tiger.lex.java chap2\programming\Yylex.java
cd chap2\programming\