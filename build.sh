cd ./src
rm -rf *.class
javac -cp ./lib/jogamp/jar/*:./lib/jogamp/jar/atomic/*:. *.java
jar cfm Program.jar ../out/MANIFEST.mf . ../lib/jogamp/jar
mv -f Program.jar ../
cd ../
java -jar Program.jar
read pause