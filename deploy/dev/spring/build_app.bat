@echo off
setlocal

cd /d "..\..\src"
if errorlevel 1 exit /b 1

call gradlew.bat application:bootJar --no-daemon
if errorlevel 1 exit /b 1

cd /d "..\deploy\dev"
if errorlevel 1 exit /b 1

endlocal