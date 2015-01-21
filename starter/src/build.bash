#!/bin/bash
set -u -e
echo "Building..."
javac *.java
echo "Running..."
java Main
