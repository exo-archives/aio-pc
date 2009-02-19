call mvn clean exopc:deploy -Ddeploy=ear
call mvn -f pom-jboss-ear.xml  install antrun:run
pause null