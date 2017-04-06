# About

  This project is a **Grace programming language Compiler**, developed at **Spring of 2017** by
  **Chrysinas Ioannis** and **Kamaras Georgios** as a semester-long group-project for Compilers course.

## Current Version (version 1.0)

  ***Stable Version for Parts I and II of the project***
  >Our compiler is currently on the *first stage* of it's development. For now, it has *Lexer and Parser capabilities*.
  It processes the argument's program and, after it decides that it is correct (meaning that it conforms with our grammar's
  specifications), it prints it's Parsing Tree. If it decides that the program is not correct, it produces some
  basic error messages to help the user-programmer find what's wrong and correct it.
  
# Specifications

  Grace is a simple imperative programming language. Her main attributes, in short, are the following:
  * Simple structure and syntax of commands and expressions, which really resembles C.
  * Basic data types for characters, integers and arrays.
  * Simple functions, pass by value and by reference.
  * Variables scopes like Pascal.
  * Library of functions.

# Instructions

## Development Platform

   * Linux Ubuntu 16.04 (and Linux Mint)
   * Apache Maven 3.3.9
   * Java version: 1.8.0_121, vendor: Oracle Corporation
   * Default locale: en_US, platform encoding: UTF-8
   * OS name: "linux", version: "4.4.0-72-generic", arch: "amd64", family: "unix"

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
   
   ```>mvn clean package```
   
   *Execute:*
   
   ```>java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main examples/hello.grace```
    
   **Important Note**
   >The name (or the path, if not in the same directory) of the file of grace code that we want to compile, must be included in the execution's command, like we do in other compilers, e.g. the gcc. In our example, the grace-code file can be found in the path ```examples/hello.grace```.
   
### Output Example

   ```
   >java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main examples/hello.grace 
   
   Printing Parsing Tree:
   
   program :
       function :
           header("hello ") :
               retType :"nothing"
           code-block body :
               matchedStmt :
                   otherMatched :
                       funcOtherStmt :
                           func-call( "puts " ) :
                               func-args :
                                   termExpr :
                                       factorTerm :
                                           lValueFactor :
                                               ""Hello world!\n" "
   ```

## Feedback

  Please see the ```AUTHORS``` file for Contact & Feedback details.