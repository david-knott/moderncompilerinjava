set CLASSPATH=.;D:\Workspace\moderncompilerinjava\compiler\JLex;D:\Workspace\moderncompilerinjava\compiler\java_cup;D:\Workspace\moderncompilerinjava\compiler\java_cup\runtime;

cd d:\Workspace\moderncompilerinjava\compiler\JLex
javac Main.java
cd d:\Workspace\moderncompilerinjava\compiler\
del /Q Parse\Yylex.java
move Parse\Tiger.lex.java Parse\Yylex.java
java JLex.Main Parse\Tiger.lex

java java_cup.Main -parser Grm -expect 3 -dump_grammar -dump_states Parse\Grm.cup
move Grm.java Parse

javac ErrorMsg/*.java Parse/*.java