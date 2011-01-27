mvn clean exopc:deploy -Ddeploy=ear
mvn -f pom-jonas.xml install antrun:run
read