@echo off
REM Socratic Java Mentor - Windows Launcher
REM This script checks for Java and launches the application

echo ========================================
echo   Socratic Java Mentor
echo   Interactive Java Learning Platform
echo ========================================
echo.

REM Check if Java is installed
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Java is not installed or not in PATH
    echo.
    echo Please install Java Development Kit (JDK) 17 or higher from:
    echo https://adoptium.net/
    echo.
    pause
    exit /b 1
)

echo Java found. Starting application...
echo.

REM Check if JAR exists
if not exist "target\socratic-java-mentor-shaded.jar" (
    echo ERROR: Application JAR not found!
    echo.
    echo Please build the project first:
    echo   mvn clean package
    echo.
    pause
    exit /b 1
)

REM Launch the application
java -jar target\socratic-java-mentor-shaded.jar

REM If there was an error
if %errorlevel% neq 0 (
    echo.
    echo ERROR: Application failed to start
    echo.
    pause
)

exit /b 0
