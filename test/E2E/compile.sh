#!/bin/bash
JAVA_HOME=/home/david/bin/jdk-14+36/bin
#JAVA_HOME=/usr/bin
shopt -s nullglob
rm -rf ./good/*.out
rm -rf ./good/*.s
$JAVA_HOME/javac -sourcepath ../../compiler/ -d ../../bin ../../compiler/**/*.java
$JAVA_HOME/java -cp ../../bin Main.Main $1 > $1.s
#gcc -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar $1.s ./runtime.c -o $1.out
#./$f.out
