cd d:\Workspace\moderncompilerinjava\src\
del /Q chap3\programming\Parse\Yylex.java
java JLex.Main chap3\programming\Parse\Tiger.lex
move chap3\programming\Parse\Tiger.lex.java chap3\programming\Parse\Yylex.java

cd D:\Workspace\moderncompilerinjava\
java java_cup.Main -parser Grm -expect 3 -dump_grammar -dump_states <src\chap3\programming\Parse\Grm.cup >Grm.out 2>Grm.err
del src\chap3\programming\Parse\Grm.out
del src\chap3\programming\Parse\sym.java
del src\chap3\programming\Parse\Grm.java
move Grm.out src\chap3\programming\Parse\
move Grm.err src\chap3\programming\Parse\
move sym.java src\chap3\programming\Parse\
move Grm.java src\chap3\programming\Parse\
cd src\chap3\programming\Parse\