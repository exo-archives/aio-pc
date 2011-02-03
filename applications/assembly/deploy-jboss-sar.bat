call mvn clean exopc:deploy -Ddeploy=ear
call mvn install antrun:run -Pjboss-sar
pause null