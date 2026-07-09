# Admin Setup Script
# Run this script after starting the backend to create the admin user

Write-Host "================================" -ForegroundColor Cyan
Write-Host "  CCTV Admin User Setup" -ForegroundColor Cyan
Write-Host "================================" -ForegroundColor Cyan
Write-Host ""

Write-Host "Checking backend status..." -ForegroundColor Yellow

try {
    $response = Invoke-WebRequest -Uri "http://localhost:8080/api/products" -Method GET -TimeoutSec 5 -ErrorAction Stop
    Write-Host "✓ Backend is running on port 8080" -ForegroundColor Green
} catch {
    Write-Host "✗ Backend is not running!" -ForegroundColor Red
    Write-Host "Please start the backend first:" -ForegroundColor Yellow
    Write-Host "  cd backend" -ForegroundColor White
    Write-Host "  mvn spring-boot:run" -ForegroundColor White
    Write-Host ""
    Read-Host "Press Enter to exit"
    exit
}

Write-Host ""
Write-Host "Creating admin user..." -ForegroundColor Yellow

try {
    $result = Invoke-RestMethod -Uri "http://localhost:8080/api/auth/setup-admin" -Method POST -ContentType "application/json"
    
    Write-Host ""
    Write-Host "================================" -ForegroundColor Green
    Write-Host "  ✅ Admin User Created!" -ForegroundColor Green
    Write-Host "================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Login Details:" -ForegroundColor Cyan
    Write-Host "  URL: http://localhost:5173/admin/login" -ForegroundColor White
    Write-Host "  Email: admin@cctv.com" -ForegroundColor White
    Write-Host "  Password: admin123" -ForegroundColor White
    Write-Host ""
    Write-Host "Admin can access:" -ForegroundColor Cyan
    Write-Host "  • Dashboard - Overview and statistics" -ForegroundColor White
    Write-Host "  • Add Products - Manage product catalog" -ForegroundColor White
    Write-Host "  • Inventory - Track stock levels" -ForegroundColor White
    Write-Host "  • Reports - View business analytics" -ForegroundColor White
    Write-Host ""
    
} catch {
    $statusCode = $_.Exception.Response.StatusCode.value__
    
    if ($statusCode -eq 409) {
        Write-Host ""
        Write-Host "================================" -ForegroundColor Green
        Write-Host "  ✅ Admin User Already Exists!" -ForegroundColor Green
        Write-Host "================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Login Details:" -ForegroundColor Cyan
        Write-Host "  URL: http://localhost:5173/admin/login" -ForegroundColor White
        Write-Host "  Email: admin@cctv.com" -ForegroundColor White
        Write-Host "  Password: admin123" -ForegroundColor White
        Write-Host ""
    } else {
        Write-Host ""
        Write-Host "✗ Error creating admin user:" -ForegroundColor Red
        Write-Host $_.Exception.Message -ForegroundColor Red
        Write-Host ""
    }
}

Write-Host "Press Enter to exit..." -ForegroundColor Gray
Read-Host
