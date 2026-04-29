@echo off
setlocal

cd /d "..\..\src"
if errorlevel 1 exit /b 1

call gradlew.bat utils-bdfiller:bootJar --no-daemon
if errorlevel 1 exit /b 1

java -jar utils-bdfiller\build\libs\utils-bdfiller.jar
if errorlevel 1 exit /b 1

cd /d "..\deploy\dev"
if errorlevel 1 exit /b 1

endlocal