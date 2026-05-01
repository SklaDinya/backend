@echo off
setlocal enabledelayedexpansion
copy /Y ..\..\src\persistence-postgres\src\main\resources\postgres-schema.sql .\postgres\scripts\schema.sql
@REM copy /Y ..\..\src\utils-bdfiller\generatedData\main.sql .\postgres\scripts\fill.sql
if errorlevel 1 exit /b %errorlevel%