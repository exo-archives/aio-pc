call mvn clean exopc:deploy -Ddeploy=ear
call mvn -f pom-jboss-sar.xml  install antrun:run
pause null