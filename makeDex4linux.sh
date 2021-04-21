rm -rf classes/
mkdir classes
find . -name "*.java">sources.txt
javac -encoding utf-8 -Xlint:unchecked  -classpath android.jar:android-support-v4.jar:nanohttpd-2.3.1.jar:fastjson-1.2.9.jar:org.apache.http.legacy.jar -d classes @sources.txt
cp nanohttpd-2.3.1.jar classes/
cp okhttp-3.12.6.jar classes/
cp okio-1.15.0.jar classes/
cp fastjson-1.2.9.jar classes/
cd classes/
jar xvf nanohttpd-2.3.1.jar
jar xvf okhttp-3.12.6.jar
jar xvf okio-1.15.0.jar
jar xvf fastjson-1.2.9.jar
rm -rf *.jar
jar cvf merge.jar .
cd ..
java -jar jarjar-1.3.jar process rule.txt classes/merge.jar classes/xradar.jar
rm classes/merge.jar
#替换你本地的dx路径
alias dx=/home/stephen/Android/Sdk/build-tools/30.0.1/dx
dx --dex --output=classes/radar.dex classes/xradar.jar
echo "xradar.jar 用于你的爬虫工程."
echo "radar.dex用于替换hooker根目录下的radar.dex"
