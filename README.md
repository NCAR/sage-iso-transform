# sage-iso-transform

A small command line application that will take a new iso document and transform it to an old iso document.

## Packaging

This program uses a self contained jar that is created using the [spring-boot-maven-plugin](http://docs.spring.io/spring-boot/docs/current/maven-plugin/).
To create the .jar file run the following maven command:

```
$ mvn package spring-boot:repackage
```
The jar file should appear in a local subdirectory within the Docker container called "target".


Or, alternatively, you may run the following docker commands:
```
docker compose -f docker-compose.yml build --build-arg PUSH_TOKEN=$(cat .github_token) 

docker compose -f docker-compose.yml up 

```

The github token is for enabling push access to the target "old ISO" WAF.  If you don't need push access and just want to test the transform operator, you can delete the lines in Dockerfile that use this token.
  
## Usage
```
$ java -jar iso-transform-command-line.jar --input=<NEW_ISO_FILE> --output=<OLD_ISO_FILE>
```

### Examples
```
$ java -jar iso-transform-command-line.jar --input=/home/vagrant/dset-web-accessible-folder-iso19115-3/eol/102.009_HLY-07-01_SCS_Underway_Sensor_Data_ISO-19115-3.xml --output=/home/vagrant/dset-web-accessible-folder-dev/eol/102.009_HLY-07-01_SCS_Underway_Sensor_Data_ISO-19115-3.xml
```

To get the exit status indicator of either success (0) or failure (1) from inside a script:
```
java -jar iso-transform-command-line.jar --input=<NEW_ISO_FILE> --output=<OLD_ISO_FILE>
result=$?
echo $result
```
