@echo off
setlocal enabledelayedexpansion

echo ==========================================================================
echo SLAWME - Smart Logistics ^& Warehouse Management Engine Build Script
echo ==========================================================================

REM Locate javac and java
set JAVA_BIN=
if exist "C:\Users\aryan\.vscode\PYTHON\Oracle_JDK-22\bin\javac.exe" (
    set "JAVA_BIN=C:\Users\aryan\.vscode\PYTHON\Oracle_JDK-22\bin"
) else (
    where javac >nul 2>nul
    if %errorlevel% equ 0 (
        set JAVA_BIN=
    ) else (
        echo ERROR: javac not found in PATH or standard location.
        exit /b 1
    )
)

if "%JAVA_BIN%"=="" (
    set JAVAC_CMD=javac
    set JAVA_CMD=java
) else (
    set "JAVAC_CMD=%JAVA_BIN%\javac.exe"
    set "JAVA_CMD=%JAVA_BIN%\java.exe"
)

echo Using Compiler: "%JAVAC_CMD%"

if not exist bin mkdir bin

echo Compiling Java Main Source Files...
"%JAVAC_CMD%" -d bin src\main\java\com\vityarthi\slawme\*.java src\main\java\com\vityarthi\slawme\exception\*.java src\main\java\com\vityarthi\slawme\model\*.java src\main\java\com\vityarthi\slawme\service\*.java src\main\java\com\vityarthi\slawme\util\*.java
if %errorlevel% neq 0 (
    echo ❌ Compilation Failed!
    exit /b %errorlevel%
)

echo Compiling Unit Tests...
"%JAVAC_CMD%" -cp bin -d bin src\test\java\com\vityarthi\slawme\*.java
if %errorlevel% neq 0 (
    echo ❌ Test Compilation Failed!
    exit /b %errorlevel%
)

echo ✓ Compilation Succeeded!

if "%1"=="test" (
    echo Running Automated Unit Tests...
    "%JAVA_CMD%" -ea -cp bin com.vityarthi.slawme.TestRunner
    exit /b %errorlevel%
)

if "%1"=="demo" (
    echo Running Automated CLI Demo Showcase...
    "%JAVA_CMD%" -cp bin com.vityarthi.slawme.Main --demo
    exit /b %errorlevel%
)

if "%1"=="run" (
    echo Launching Interactive CLI Application...
    "%JAVA_CMD%" -cp bin com.vityarthi.slawme.Main
    exit /b %errorlevel%
)

echo.
echo Build Complete! Usage options:
echo   build.bat run   - Launch Interactive CLI Application
echo   build.bat demo  - Run Automated Showcase Demo
echo   build.bat test  - Run Automated Unit Test Suite
