# build
cd ../..
mvn clean install
cd applications/assembly

# deploy
echo "Removing..."
rm -r -f ../../../../../../../exo-working/exo-tomcat/
echo "Removed."
mkdir ../../../../../../../exo-working/exo-tomcat/
echo "Copying..."
cp -r ../../../../../../../exo-dependencies/tomcat-6.0.16/* ../../../../../../../exo-working/exo-tomcat/
echo "Copied."

echo "Deploying..."
mvn clean exopc:deploy -Ddeploy=tomcat6
echo "Deployed."

read
