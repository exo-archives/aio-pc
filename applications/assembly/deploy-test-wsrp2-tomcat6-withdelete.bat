rmdir /s /q ..\..\..\..\..\..\exo-working\exo-tomcat\
mkdir ..\..\..\..\..\..\exo-working\exo-tomcat\
xcopy /e /h /r ..\..\..\..\..\..\exo-dependencies\tomcat-6.0.13\* ..\..\..\..\..\..\exo-working\exo-tomcat\*

call mvn -f pom-test-wsrp2.xml clean exopc:deploy -Ddeploy=tomcat
pause null