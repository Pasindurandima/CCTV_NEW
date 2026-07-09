@echo off
echo ================================
echo   Starting CCTV Backend Server
echo ================================
echo.

cd /d "%~dp0backend"

echo Checking for existing Java processes...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 2 /nobreak >nul

echo Starting backend server...
echo Backend will run on: http://localhost:8080
echo.
echo Press Ctrl+C to stop the server
echo.

mvn spring-boot:run
