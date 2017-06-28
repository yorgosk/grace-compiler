# About

  This project is a **Grace programming language Compiler**, developed at **Spring of 2017** by
  **Chrysinas Ioannis** and **Kamaras Georgios** as a semester-long group-project for Compilers course.
  The project's development is going to progress in three stages, during the course of the semester:
  1. The first stage _(Parts I and II)_ involves the creation of the compiler's Lexical and Syntactical Analyser.
  2. The second stage _(Parts III and IV)_ involves creating the compiler's Semantical Analyser and generating Intermediate Code.
  3. The third, and final, stage _(Parts V and VI -- current stage)_ is about Optimization and Program Execution.

## Current Version (version 2.0)

  ***Stable Version for second stage _(Parts III and IV)_ of the project***
  >Our compiler is currently in the beginning of the third stage of it's development. For now, it only has complete
  *Lexing, Parsing, Semantic Analysing and Intermediate Code production* capabilities. It processes a program, given to
  it's script (grace_compiler.sh) as a command line argument and produces an accurate Low Level Intermediate
  Representation which in a large part can easily be transformed to assembly x8086 commands. However, the produced
  assembly code most of the time is not accurate or complete. (But, works for hello.grace)
  
  **Important Note**
  Due to time constraints regarding other courses' assignments the produced assembly code most of the time is not 
  accurate or complete. (But, works for hello.grace)
  
  **Important Files**
  * at ```src/main/java/compiler/Main.java``` we have our **Main class** from which we read the Grace code from a
  *text file* (*.grace*, *.txt*, etc) passed as an argument in the execution command *or* from the *stdin*, if no
  argument was given, we process it to spot Lexer, Parser or IO Exceptions that it may create and we load it's
  Syntactical components in a Parsing Tree, which we print in the end.
  * at ```src/main/java/compiler/PTPrintingVisitor.java``` we have our **PTPrintingVisitor** class, in which
  we use the *Visitor Pattern* to traverse and print our Parsing Tree (PT) in an efficient and easily maintainable way.
  * at ```src/main/sablecc/parser.grammar``` we have our **Grammar's** file, created using the *SableCC Framework*.
  * at ```src/main/java/compiler/ASTPrintingVisitor.java``` we have our **ASTPrintingVisitor** class, in which
    we use the *Visitor Pattern* to traverse and print our Abstract Syntax Tree (AST) in an efficient and easily maintainable way.
  * at ```src/main/java/compiler/SymbolTable.java``` we have our **SymbolTable** class, which is the core of our Symbol 
  Tables implementation. To improve our project structure we have included many utility functions and structures, which
  can be found at ```src/main/java/compiler/STRecord.java``` and ```src/main/java/compiler/NSRecord.java```.
  * at ```src/main/java/compiler/IntermediateCode.java``` we have our **IntermediateCode** class, which is the core of our
   LLIR code production infrastructure implementation.
  * at ```src/main/java/compiler/MachineCode.java``` we have our **MachineCode** class, which is the core of our
   Assembly x8086 code production infrastructure implementation.
  * at ```src/main/java/compiler/ActivationRecord.java``` we have our **ActivationRecord** class, which can be used to
   simulate the Activation Records exchanges that take place in the background while running our program. It has not
   been used in our implementation, but it is a nice utility, so we included it.
  * with ```grace_compiler.sh <input> [flags]``` our Grace compiler can be used in a variety of ways illustrated at
   *To use the Grace Compiler* section of this document. **Careful**, a chmod command like ```chmod 777``` may need to
   be applied on the script to make it runnable to your computer.

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

## To use the Grace Compiler

   **Build compiler and execute up-to Assembly x8086 production**
   ```./grace_compiler.sh <name of .grace file>```
   
   **Build compiler and execute up-to Low Level Intermediate Representation production**
   ```./grace_compiler.sh <name of .grace file> --upto-ir```
   
   **Just execute up-to Assembly x8086 production (no rebuild)**
   ```./grace_compiler.sh <name of .grace file> --no-rebuild```
   
   ***RECOMMENDED BASED ON OUR LEVEL OF PROGRESS***
   **Just execute up-to Low Level Intermediate Representation production (no rebuild)**
   ```./grace_compiler.sh <name of .grace file> --no-rebuild --upto-ir```

### Commands Example

   **Build compiler and execute up-to Assembly x8086 production**
   ```./grace_compiler.sh examples/general/hello.grace```
     
   **Build compiler and execute up-to Low Level Intermediate Representation production**
   ```./grace_compiler.sh examples/general/hello.grace --upto-ir```
      
   **Just execute up-to Assembly x8086 production (no rebuild)**
   ```./grace_compiler.sh examples/general/hello.grace --no-rebuild```
   
   ***RECOMMENDED BASED ON OUR LEVEL OF PROGRESS***
   **Just execute up-to Low Level Intermediate Representation production (no rebuild)**
   ```./grace_compiler.sh examples/general/hello.grace --no-rebuild --upto-ir```
    
   **Important Notes**
   >The name (or the path, if not in the same directory) of the file of grace code that we want to compile, must be 
   included in the execution's command, like we do in other compilers, e.g. the gcc. In our example, the grace-code file
   can be found in the path ```examples/general/hello.grace```.
   
### Output Example (version 2.0)

   ```
   Intermediate Representation:
   
                   Low Level Intermediate Representation:
   1: unit, hello, -, -
   2: par, "Hello world!\n", R, -
   3: call, -, -, puts
   4: endu, hello, -, -
   Hello world!
   
   ```

## Feedback

  Please see the ```AUTHORS``` file for Contact & Feedback details.