@echo off
echo ================================
echo   Testing Backend Connection
echo ================================
echo.

powershell -Command "try { $response = Invoke-WebRequest -Uri 'http://localhost:8080/api/products' -Method GET -TimeoutSec 5; Write-Host '✅ Backend is RUNNING!' -ForegroundColor Green; Write-Host 'Status Code:' $response.StatusCode -ForegroundColor White; Write-Host ''; Write-Host 'Admin Login URL: http://localhost:5173/admin/login' -ForegroundColor Cyan; Write-Host 'Email: admin@cctv.com' -ForegroundColor White; Write-Host 'Password: admin123' -ForegroundColor White; } catch { Write-Host '❌ Backend is NOT running!' -ForegroundColor Red; Write-Host ''; Write-Host 'Please start the backend using start-backend.bat' -ForegroundColor Yellow; }"

echo.
pause
