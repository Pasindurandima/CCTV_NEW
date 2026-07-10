@echo off
echo ============================================
echo   Multiple Images Setup and Backend Start
echo ============================================
echo.

REM Step 1: Run database migration
echo Running migration using Aiven database...
mysql -h mysql-36042108-pasirandima2001-745a.c.aivencloud.com -P 28247 -u avnadmin -pAVNS_0t-CImdsGZPopmBRpRW --ssl-mode=REQUIRED defaultdb < add_multiple_images.sql
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Database migration failed!
    echo Note: If columns already exist, this is normal. Continuing...
) else (
    echo SUCCESS: Database migration completed!
)
echo.

REM Step 2: Clean and compile backend
echo [Step 2/3] Compiling backend...
call mvn clean compile
if %ERRORLEVEL% NEQ 0 (
    echo ERROR: Backend compilation failed!
    pause
    exit /b 1
)
echo SUCCESS: Backend compiled successfully!
echo.

REM Step 3: Start backend server
echo [Step 3/3] Starting backend server...
echo Backend will start on http://localhost:8080
echo Press Ctrl+C to stop the server
echo.
call mvn spring-boot:run
