rm -rf libs/
../gradlew makeJar
cp nanohttpd-2.3.1.jar libs/
cp okhttp-3.12.6.jar libs/
cp okio-1.15.0.jar libs/
cd libs
jar xvf radar.jar
jar xvf nanohttpd-2.3.1.jar
jar xvf okhttp-3.12.6.jar
jar xvf okhttp-3.12.6.jar
jar xvf okio-1.15.0.jar
rm -rf *.jar
jar cvf radar.jar .
cd ..
java -jar jarjar-1.3.jar process rule.txt libs/radar.jar libs/xradar.jar
rm -f libs/radar.jar
dx --dex --output=libs/radar.dex libs/xradar.jar