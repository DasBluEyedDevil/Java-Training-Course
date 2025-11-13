#!/bin/bash
# Socratic Java Mentor - Unix/Linux/Mac Launcher
# This script checks for Java and launches the application

echo "========================================"
echo "  Socratic Java Mentor"
echo "  Interactive Java Learning Platform"
echo "========================================"
echo ""

# Check if Java is installed
if ! command -v java &> /dev/null; then
    echo "ERROR: Java is not installed or not in PATH"
    echo ""
    echo "Please install Java Development Kit (JDK) 17 or higher from:"
    echo "https://adoptium.net/"
    echo ""
    exit 1
fi

# Display Java version
echo "Java found:"
java -version
echo ""

# Check if JAR exists
if [ ! -f "target/socratic-java-mentor-shaded.jar" ]; then
    echo "ERROR: Application JAR not found!"
    echo ""
    echo "Please build the project first:"
    echo "  mvn clean package"
    echo ""
    exit 1
fi

echo "Starting application..."
echo ""

# Launch the application
java -jar target/socratic-java-mentor-shaded.jar

# Check exit status
if [ $? -ne 0 ]; then
    echo ""
    echo "ERROR: Application failed to start"
    echo ""
    exit 1
fi

exit 0
