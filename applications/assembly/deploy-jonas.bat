call mvn clean exopc:deploy -Ddeploy=ear
call mvn install antrun:run -Pjonas
pause null