@echo off
setlocal

REM Get current directory for this script
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM End previous session
docker compose -f .\docker-compose.yaml down

REM Redis - no actions to prepare

REM Postgres - build and copy
call postgres\generate_data.bat
call postgres\copy_scripts.bat

REM Spring - build and copy
call spring\build_app.bat
call spring\copy_files.bat

REM Start
docker compose -f .\docker-compose.yaml up -d

endlocal