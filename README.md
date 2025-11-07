# sage-iso-transform

A small command line application that will take a new iso document and transform it to an old iso document.

## Packaging

This program uses a self containted jar that is created using the [spring-boot-maven-plugin](http://docs.spring.io/spring-boot/docs/current/maven-plugin/).
To create the .jar file run the following maven command:

```
$ mvn package spring-boot:repackage
```

Or, alternatively, you may run the following docker command:
```
docker build -t iso-transformer .
```
Then use Docker dashboard to run the container and open a terminal session within it. 

## Deployment

Download the jar file from Archiva Repository using curl or wget.

### Snapshots
https://archiva.ucar.edu/repository/sage-package-snapshots/sgf/iso-transform-command-line/

### Releases
https://archiva.ucar.edu/repository/sage-package-release/sgf/iso-transform-command-line/

### Example
```
$ curl https://archiva.ucar.edu/repository/sage-package-snapshots/sgf/iso-transform-command-line/0.0.2-SNAPSHOT/iso-transform-command-line-0.0.2-20191028.161503-1.jar --output iso-transform-command-line.jar
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
