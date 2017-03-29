# sage-iso-transform

A small command line application that will take a new iso document and transform it to an old iso document.

## Packaging

This program uses a self containted jar that is created using the [spring-boot-maven-plugin](http://docs.spring.io/spring-boot/docs/current/maven-plugin/).
To create the .jar file run the following maven command:

```
$ mvn package spring-boot:repackage
```


## Deployment

Download the jar file from Artifactory using curl or wget.

### Snapshots
https://repo.ucar.edu/artifactory/apps-snapshot-local/sgf/iso-transform-command-line/

### Releases
https://repo.ucar.edu/artifactory/apps-release-local/sgf/iso-transform-command-line/

### Example
```
$ curl https://repo.ucar.edu/artifactory/apps-snapshot-local/sgf/iso-transform-command-line/0.0.1-SNAPSHOT/iso-transform-command-line-0.0.1-20170327.162751-5.jar --output iso-transform-command-line.jar
```
  
  
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