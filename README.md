# About

  This is a compiler for the Grace programming language, developed at Spring of 2017 by
  Chrysinas Ioannis and Kamaras Georgios as a semester-long project for Compilers course.

# Instructions

## Development Platform

   Lunux Ubuntu 16.04
   Apache Maven 3.3.9
   Java version: 1.8.0_121, vendor: Oracle Corporation
   Default locale: en_US, platform encoding: UTF-8
   OS name: "linux", version: "4.4.0-72-generic", arch: "amd64", family: "unix"

## System Requirements

   Having ```maven``` and ```java``` installed.

## To compile

   mvn compile

## To create a packaged JAR file

   mvn package

## To clean

   mvn clean
   
## Example Run

### Commands Example

   *Build:*
   
   ```> mvn clean package```
   
   *Execute:*
   
   ```> java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main examples/bsort.grace```
    
   **Important Note**
   >The name (or the path, if not in the same directory) of the file of grace code that we want to compile, must be included in the execution's command, like we do in other compilers, e.g. the gcc. In our example, the grace-code file can be found in the path ```examples/bsort.grace```.
   
### Output Example

## Feedback

  Please see the ```AUTHORS``` file for Contact & Feedback details.