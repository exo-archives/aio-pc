call mvn clean exopc:deploy -Ddeploy=ear
call mvn -f pom-jonas.xml  install antrun:run
pause null