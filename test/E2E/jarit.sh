#!/bin/bash
JAVA_HOME=/home/david/bin/jdk-14+36/bin
#JAVA_HOME=/usr/bin
$JAVA_HOME/javac -sourcepath ../../compiler/ -d ../../bin ../../compiler/**/*.java
$JAVA_HOME/jar --create --file tc.jar --main-class Main.Main -C ../../bin . 

