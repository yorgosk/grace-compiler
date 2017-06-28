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
	elif [ $2 = "--upto-ir" ]
		then	
		java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
	else
		echo "Unsupported second argument!"
		exit -2
	fi
elif [ $# -eq 3 ]
	then
	rebFlag=0
	irFlag=0
	if [ $2 = "--no-rebuild" ]
		then
		let "rebFlag = rebFlag+1"
		java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
		gcc -m32 -o executable g.s
		./executable
		rm executable
	elif [ $2 = "--upto-ir" ]
		then
		let "irFlag = irFlag+1"
		java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
	else
		echo "Unsupported second argument!"
		exit -2
	fi
	if [ $2 = "--upto-ir" ]
		then
		let "rebFlag = rebFlag+1"
		java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
		gcc -m32 -o executable g.s
		./executable
		rm executable
	elif [ $2 = "--no-rebuild" ]
		then
		let "irFlag = irFlag+1"
		java -cp target/compiler-1.0-SNAPSHOT.jar compiler.Main $1
	else
		echo "Unsupported second argument!"
		exit -2
	fi
	
	if (( rebFlag != 1 )) || (( irFlag != 1 ))	# if there is a flag missing
	then
		echo "ERROR! Bad patamerers!"
		exit -1
	fi
else
	echo "ERROR! Too many arguments!"
	exit -3
fi
