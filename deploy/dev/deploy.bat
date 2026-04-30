@echo off
setlocal

REM Get current directory for this script
set "SCRIPT_DIR=%~dp0"
cd /d "%SCRIPT_DIR%"

REM End previous session
docker compose -f .\docker-compose-win.yaml down

REM Redis - no actions to prepare

REM Postgres - copy
call postgres\copy_scripts.bat

REM Spring - copy
call spring\copy_files.bat

REM Start
docker compose -f .\docker-compose-win.yaml up -d

endlocal