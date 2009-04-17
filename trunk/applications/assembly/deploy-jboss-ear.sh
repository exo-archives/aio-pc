mvn clean exopc:deploy -Ddeploy=ear
mvn -f pom-jboss-ear.xml install antrun:run
read