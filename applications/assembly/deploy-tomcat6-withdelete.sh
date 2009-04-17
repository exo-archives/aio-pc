# deploy
echo "Removing..."
rm -r -f ../../../../../../exo-working/exo-tomcat/
echo "Removed."
mkdir ../../../../../../exo-working/exo-tomcat/
echo "Copying..."
cp -r ../../../../../../exo-dependencies/tomcat-6.0.18/* ../../../../../../exo-working/exo-tomcat/
echo "Copied."

mvn clean exopc:deploy -Ddeploy=tomcat6

read
