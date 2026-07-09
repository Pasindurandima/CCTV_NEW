@echo off
echo ================================
echo   CCTV E-Commerce System
echo ================================
echo.

echo [1/3] Stopping any existing processes...
taskkill /F /IM java.exe >nul 2>&1
timeout /t 1 /nobreak >nul

echo [2/3] Starting Backend Server...
cd /d "%~dp0backend"
start "CCTV Backend" cmd /k "echo Backend Server Starting... && mvn spring-boot:run"

echo [3/3] Waiting for backend to start...
timeout /t 12 /nobreak

echo.
echo Testing backend connection...
powershell -Command "try { Invoke-WebRequest -Uri 'http://localhost:8080/api/products' -Method GET -TimeoutSec 5 | Out-Null; Write-Host '✅ Backend is running!' -ForegroundColor Green; } catch { Write-Host '⚠️ Backend may still be starting...' -ForegroundColor Yellow; }"

echo.
echo ================================
echo   System Status
echo ================================
echo Backend: http://localhost:8080
echo Frontend: http://localhost:5173
echo Admin Login: http://localhost:5173/admin/login
echo.
echo Admin Credentials:
echo   Email: admin@cctv.com
echo   Password: admin123
echo.
echo To start frontend, run: cd frontend ^&^& npm run dev
echo.
pause
