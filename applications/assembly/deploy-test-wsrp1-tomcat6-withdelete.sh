rm -r -f ../../../../../../exo-working/exo-tomcat/
echo "Removed."
mkdir ../../../../../../exo-working/exo-tomcat/
echo "Copying..."
cp -r ../../../../../../exo-dependencies/apache-tomcat-6.0.26/* ../../../../../../exo-working/exo-tomcat/
echo "Copied."

mvn install -Ddeploy=tomcat6 -Ptest-wsrp1

#mvn clean exopc:deploy -Ddeploy=tomcat6 ant:ant -Ptest-wsrp1

read
