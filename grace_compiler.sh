#!/bin/bash

# example: %>./script.sh examples/general/primes.grace

if [ $# -eq 0 ]		# if no arguments were supplied
  	then
    	echo "ERROR! No arguments supplied!"
    	exit -1
elif [ $# -eq 1 ]
	then
	mvn clean package
	java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
	gcc -m32 -o executable g.s
	./executable
	rm executable
elif [ $# -eq 2 ]
	then
	if [ $2 = "--no-rebuild" ]
		then	
		java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
		gcc -m32 -o executable g.s
		./executable
		rm executable
	else
		echo "Unsupported second argument!"
		exit -2
	fi
else
	echo "ERROR! Too many arguments!"
	exit -3
fi
