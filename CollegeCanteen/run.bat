@echo off
REM Double-click this file (Windows) to compile and run the project.
if not exist out mkdir out
dir /s /b src\*.java > sources.txt
javac -d out @sources.txt
if errorlevel 1 (
    echo.
    echo Compilation failed. Read the errors above.
    pause
    exit /b 1
)
java -cp out Main
pause
