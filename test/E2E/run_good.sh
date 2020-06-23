#!/bin/bash
JAVA_HOME=/home/david/bin/jdk-14+36/bin
#JAVA_HOME=/usr/bin
shopt -s nullglob
rm -rf ./good/*.out
rm -rf ./good/*.s
$JAVA_HOME/javac -sourcepath ../../compiler/ -d ../../bin ../../compiler/**/*.java
for f in ./good/*.tig; do

$JAVA_HOME/java -cp ../../bin Main.Main $f
gcc -g -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar $f.s ./runtime.c -o $f.out
ACTUAL=$($f.out)
EXPECTED=$(cat $f.result)
if [ "$ACTUAL" != "$EXPECTED" ]
then
echo -e "\e[91mTest Result: $f failed.\e[0m"
else
echo -e "\e[92mTest Result: $f passed.\e[0m"
fi
done
