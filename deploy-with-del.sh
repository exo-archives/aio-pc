 #cd applications/test-portal/
#mvn clean install
#cd ../..

cd applications/assembly/

rm -r -f ../../../../../../exo-working/exo-tomcat/
mkdir ../../../../../../exo-working/exo-tomcat/
cp -r ../../../../../../exo-dependencies/apache-tomcat-6.0.18/* ../../../../../../exo-working/exo-tomcat/

mvn clean exopc:deploy -Ddeploy=tomcat6

read
