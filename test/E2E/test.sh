#!/bin/bash
shopt -s nullglob
/home/david/bin/jdk-14+36/bin/javac -sourcepath ../../compiler/ -d ../../bin ../../compiler/**/*.java


for f in ./tests/*.tig; do

/home/david/bin/jdk-14+36/bin/java -cp ../../bin Main.Main $f
gcc -w -no-pie -Wimplicit-function-declaration -Wl,--wrap,getchar $f.s ./runtime.c -o $f.out
ACTUAL=$($f.out)
EXPECTED=$(cat $f.result)
if [ "$ACTUAL" != "$EXPECTED" ]
then
echo "Test Result: $f failed."
else
echo "Test Result: $f passed."
fi
done
#rm -rf ./tests/*.out