
# Instructions

## To compile

   mvn compile

## To create a packaged JAR file

   mvn package

## To clean

   mvn clean

## Example Run
   *Build:*
   
   ```> mvn clean package```
   
   *Execute:*
   
   ```> java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main examples/bsort.grace```
    
   **Important Note**
   >The name (or the path, if not in the same directory) of the file of grace code that we want to compile, must be included in the execution's command, like we do in other compilers, e.g. the gcc. In our example, the grace-code file can be found in the path `examples/bsort.grace`.