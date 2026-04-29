@echo off
setlocal

copy /Y "..\..\src\persistence-postgres\src\main\resources\schema.sql" "postgres\scripts\schema.sql"
if errorlevel 1 exit /b 1

copy /Y "..\..\src\utils-bdfiller\generatedData\main.sql" "postgres\scripts\fill.sql"
if errorlevel 1 exit /b 1

endlocal