mvn clean install

cd applications/assembly/

rm -r -f ../../../../../exo-working/exo-tomcat/
mkdir ../../../../../exo-working/exo-tomcat/
cp -r ../../../../../exo-dependencies/tomcat-6.0.14/* ../../../../../exo-working/exo-tomcat/

mvn clean exopc:deploy -Ddeploy=tomcat6

read
