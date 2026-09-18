#!/usr/bin/env bash
set -e

echo "=========================================================================="
echo "SLAWME - Smart Logistics & Warehouse Management Engine Build Script (Shell)"
echo "=========================================================================="

mkdir -p bin

echo "Compiling Java Main Source Files..."
javac -d bin $(find src/main/java -name "*.java")

echo "Compiling Unit Tests..."
javac -cp bin -d bin $(find src/test/java -name "*.java")

echo "✓ Compilation Succeeded!"

if [ "$1" == "test" ]; then
    echo "Running Automated Unit Tests..."
    java -ea -cp bin com.vityarthi.slawme.TestRunner
elif [ "$1" == "demo" ]; then
    echo "Running Automated CLI Demo Showcase..."
    java -cp bin com.vityarthi.slawme.Main --demo
elif [ "$1" == "run" ]; then
    echo "Launching Interactive CLI Application..."
    java -cp bin com.vityarthi.slawme.Main
else
    echo "Build Complete! Usage options:"
    echo "  ./build.sh run   - Launch Interactive CLI Application"
    echo "  ./build.sh demo  - Run Automated Showcase Demo"
    echo "  ./build.sh test  - Run Automated Unit Test Suite"
fi
