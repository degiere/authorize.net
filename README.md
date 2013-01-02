authorize.net
=============
Authorize.net AIM and CIM wrapper for friendlier usage

Dependencies not in public repositories
---------------------------------------
Install running this command:
mvn install:install-file -Dfile=./lib/anet-java-sdk-1.4.6.jar -DgroupId=net.authorize -DartifactId=anet-java-sdk -Dversion=1.4.6 -Dpackaging=jar

See: http://docs.broadleafcommerce.org/current/Authorize.net-Environment-Setup.html

Configuration
-------------
add authorizenet.properties on your classpath with these entries:

login=<login>
transactionKey=<transaction key>
mode=production
test.email=yourtestemail@yourdomain.com

Running unit tests
==================
$ mvn test
$ mvn -Dtest=CustomerManagerIntegrationTest test
