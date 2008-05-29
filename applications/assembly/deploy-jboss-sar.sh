mvn clean exopc:deploy -Ddeploy=ear
mvn -f pom-jboss-sar.xml install antrun:run
read