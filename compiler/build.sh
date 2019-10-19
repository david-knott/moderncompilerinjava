#!/bin/bash
package=compiler_build_script
while test $# -gt 0; do
  case "$1" in
    -h|--help)
      echo "$package - builds compiler java code"
      echo " "
      echo "$package [options]"
      echo " "
      echo "options:"
      echo "-h, --help                show brief help"
      echo "-l                        rebuild lexer"
      echo "-p                        rebuild parser"
      exit 0
      ;;
    -l)
      shift # shift moves positional parameters to the left to allow while loop the exit
      echo "lexer"
        export LEXER='set'
      ;;
    -p)
      shift
      echo "parser"
        export PARSER='set'
      ;;
    *)
      break
      ;;
  esac
done
# where -n tests that the argument is
# not zero length
# compile jlex and build lexer
if [ -n "$LEXER" ]
then

echo ">>> JLEX"
    cd ./JLex
    javac -d ../bin Main.java
    cd ..
    java -cp bin JLex.Main Parse/Tiger.lex
    mv Parse/Tiger.lex.java Parse/Yylex.java
fi


# compile java cup and create grammar parser
if [ -n "$PARSER" ]
then
  echo ">>> Running CUP"
  javac -d bin java_cup/*.java java_cup/runtime/*.java
  java -cp bin java_cup.Main -parser Grm -expect 3 -dump_grammar -dump_states Parse/Grm.cup
  mv Grm.java Parse
  mv sym.java Parse
fi

javac -d bin Symbol/*.java Absyn/*.java ErrorMsg/*.java Parse/*.java
java -cp bin Parse.Main ../reference/tiger/testcases/test2.tig