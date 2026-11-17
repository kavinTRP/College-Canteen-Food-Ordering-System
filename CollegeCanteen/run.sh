#!/bin/bash
# Mac / Linux:  chmod +x run.sh  then  ./run.sh
mkdir -p out
javac -d out src/*.java && java -cp out Main
