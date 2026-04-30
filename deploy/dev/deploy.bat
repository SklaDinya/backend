@echo off
setlocal enabledelayedexpansion

REM End previous session
docker compose -f docker-compose-win.yaml down
if errorlevel 1 exit /b %errorlevel%

REM Redis - no actions to prepare

REM Postgres - build and copy
@REM call postgres\generate_data.bat
@REM if errorlevel 1 exit /b %errorlevel%
call postgres\copy_scripts.bat
if errorlevel 1 exit /b %errorlevel%

REM Spring - build and copy
@REM call spring\build_app.bat
@REM if errorlevel 1 exit /b %errorlevel%
call spring\copy_files.bat
if errorlevel 1 exit /b %errorlevel%

REM Start
docker compose -f docker-compose-win.yaml up -d
if errorlevel 1 exit /b %errorlevel%