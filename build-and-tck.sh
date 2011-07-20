echo 'Running TCK tests...'
mvn clean install
sleep 10
cd applications/tck-tests
mvn clean install -Ptest
