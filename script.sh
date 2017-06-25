#!/bin/bash

# example: %>./script.sh examples/general/primes.grace

if [ $# -eq 0 ]		# if no arguments were supplied
  	then
    	echo "ERROR! No arguments supplied!"
    	exit -1
fi

mvn clean package

java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
