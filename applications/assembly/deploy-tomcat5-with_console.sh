rm -r -f ../../../../../../exo-working/exo-tomcat/
mkdir ../../../../../../exo-working/exo-tomcat/
cp -r ../../../../../../exo-dependencies/tomcat-5.5.17/* ../../../../../../exo-working/exo-tomcat/

mvn -f pom-jcr-console.xml clean exopc:deploy -Ddeploy=tomcat

read
