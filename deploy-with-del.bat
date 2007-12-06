call cd applications/assembly/

rmdir /s /q ..\..\..\..\..\..\..\exo-working\exo-tomcat\
mkdir ..\..\..\..\..\..\..\exo-working\exo-tomcat\
xcopy /e /h /r ..\..\..\..\..\..\..\exo-dependencies\tomcat-5.5.17\* ..\..\..\..\..\..\..\exo-working\exo-tomcat\*

call mvn clean exopc:deploy -Ddeploy=tomcat

pause null