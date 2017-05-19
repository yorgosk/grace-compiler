# About

  This project is a **Grace programming language Compiler**, developed at **Spring of 2017** by
  **Chrysinas Ioannis** and **Kamaras Georgios** as a semester-long group-project for Compilers course.
  The project's development is going to progress in three stages, during the course of the semester:
  1. The first stage _(Parts I and II)_ involves the creation of the compiler's Lexical and Syntactical Analyser.
  2. The second stage _(Parts II and III -- current stage)_ involves creating the compiler's Semantical Analyser and generating Intermediate Code.
  3. The third, and final, stage is about Optimization and Program Execution.

## Current Version (version 1.0)

  ***Stable Version for first stage _(Parts I and II)_ of the project***
  >Our compiler is currently on the *first stage* of it's development. For now, it has *Lexer and Parser capabilities*.
  It processes the input's program and, after it decides that it is correct (meaning that it conforms with our grammar's
  specifications), it prints it's Parsing Tree. If it decides that the program is not correct, it produces some
  basic error messages to help the user-programmer find what's wrong and correct it.
  
  **Important Files**
  * at ```src/main/java/compiler/Main.java``` we have our **Main class** from which we read the Grace code from a
  *text file* (*.grace*, *.txt*, etc) passed as an argument in the execution command *or* from the *stdin*, if no
  argument was given, we process it to spot Lexer, Parser or IO Exceptions that it may create and we load it's
  Syntactical components in a Parsing Tree, which we print in the end.
  * at ```src/main/java/compiler/PTPrintingVisitor``` we have our **PTPrintingVisitor** class, in which
  we use the *Visitor Pattern* to traverse and print our Parsing Tree (PT) in an efficient and easily maintainable way.
  * at ```src/main/sablecc/parser.grammar``` we have our **Grammar's** file, created using the *SableCC Framework*.

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

   **Build:**
   
   ```>mvn clean package```
   
   **Execute:**
   
   * If compiler takes input from a file:
   
   ```>java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main examples/hello.grace```
   
   * or, if compiler takes input from stdin *(not recommended for large programs, for practical reasons)*:
   
   ```java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main```
    
   **Important Notes**
   >For the first execution option, the name (or the path, if not in the same directory) of the file of grace code that
   we want to compile, must be included in the execution's command, like we do in other compilers, e.g. the gcc. In our
   example, the grace-code file can be found in the path ```examples/hello.grace```.
   >For the second execution option, the compiler expects to receive a Ctrl^D signal to start processing the input. This
   signal should be received **in a line below the last line of the Grace code**.
   
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