@echo off
chcp 65001 >nul
title Validation Library - Live Demo

set DIR=%~dp0

where javac >nul 2>&1
if %errorlevel% equ 0 (
    set "JAVAC=javac"
    set "JAVA=java"
) else if exist "C:\Program Files\Java\jdk-24\bin\javac.exe" (
    set "JAVAC=C:\Program Files\Java\jdk-24\bin\javac.exe"
    set "JAVA=C:\Program Files\Java\jdk-24\bin\java.exe"
) else (
    echo ERROR: Java not found. Install JDK 24 from oracle.com/java
    pause
    exit /b 1
)

if not exist "%DIR%out" mkdir "%DIR%out"

"%JAVAC%" -cp "%DIR%validation.jar" -d "%DIR%out" "%DIR%src\LiveDemo.java" >nul 2>&1
if %errorlevel% neq 0 (
    echo ERROR: Compilation failed.
    pause
    exit /b 1
)

"%JAVA%" -cp "%DIR%out;%DIR%validation.jar" LiveDemo

echo.
pause
