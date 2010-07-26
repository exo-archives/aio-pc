rm -r -f ../../../../../../exo-working/exo-tomcat/
echo "Removed."
mkdir ../../../../../../exo-working/exo-tomcat/
echo "Copying..."
cp -r ../../../../../../exo-dependencies/apache-tomcat-6.0.26/* ../../../../../../exo-working/exo-tomcat/
echo "Copied."

mvn -f pom-test-wsrp2.xml deploy -Ddeploy=tomcat6

read
