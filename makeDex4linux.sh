rm -rf classes/
mkdir classes
find . -name "*.java">sources.txt
javac -encoding utf-8 -Xlint:unchecked  -classpath android.jar:android-support-v4.jar:nanohttpd-2.3.1.jar -d classes @sources.txt
cp nanohttpd-2.3.1.jar classes/
cp okhttp-3.12.6.jar classes/
cp okio-1.15.0.jar classes/
cd classes/
jar xvf nanohttpd-2.3.1.jar
jar xvf okhttp-3.12.6.jar
jar xvf okio-1.15.0.jar
rm -rf *.jar
jar cvf radar.jar .
cd ..
java -jar jarjar-1.3.jar process rule.txt classes/radar.jar classes/xradar.jar
dx --dex --output=classes/radar.dex classes/xradar.jar
rm -f classes/radar.jar
