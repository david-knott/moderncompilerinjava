#!/bin/bash
JAVA_HOME=/home/david/bin/jdk-14+36/bin
#JAVA_HOME=/usr/bin
$JAVA_HOME/javac -sourcepath ../../compiler/ -d ../../bin ../../compiler/**/*.java
$JAVA_HOME/java -cp ../../bin Main.Main  $1
# gcc -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar $1.s ./runtime.c -o $1.out
#./"$1.out"
