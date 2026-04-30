@echo off
setlocal enabledelayedexpansion
@REM copy /Y ..\..\src\application\build\libs\skladinya-application.jar spring\app.jar
if exist spring\api\ rmdir /S /Q spring\api\
xcopy /E /I /Y ..\..\docs\ spring\api\
if errorlevel 1 exit /b %errorlevel%