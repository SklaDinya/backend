@echo off
setlocal

copy /Y "..\..\src\application\build\libs\skladinya-application.jar" "spring\app.jar"
if errorlevel 1 exit /b 1

if exist "spring\api\" (
    rmdir /S /Q "spring\api"
    if errorlevel 1 exit /b 1
)

xcopy /E /I /Y "..\..\docs" "spring\api"
if errorlevel 1 exit /b 1

endlocal