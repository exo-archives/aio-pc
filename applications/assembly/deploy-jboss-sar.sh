mvn clean exopc:deploy -Ddeploy=ear
mvn install antrun:run -Pjboss-sar
read