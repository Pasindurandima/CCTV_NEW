@echo off
cls
echo ===============================================
echo  FIX: 3 Images Not Saving to Database
echo ===============================================
echo.
echo This guide will help you fix the issue step by step.
echo.
echo ===============================================
echo  STEP 1: Check Database Configuration
echo ===============================================
echo.
echo Your application.properties shows:
echo   Database Name: secucctv_db
echo.
echo IMPORTANT: You need to use MySQL Workbench to:
echo   1. Check if database 'secucctv_db' exists
echo   2. Or check if database 'cctv_shop' exists
echo   3. Use the correct database name
echo.
echo Press any key when you've verified the database name...
pause >nul
echo.

echo ===============================================
echo  STEP 2: Run Database Migration
echo ===============================================
echo.
echo Option A - Using MySQL Workbench (RECOMMENDED):
echo   1. Open MySQL Workbench
echo   2. Connect to Local instance MySQL
echo   3. Select your database (secucctv_db or cctv_shop)
echo   4. File -^> Open SQL Script
echo   5. Choose: add_multiple_images.sql
echo   6. Click Execute (lightning bolt icon)
echo   7. Check for success message
echo.
echo Option B - Using Command Line:
echo   Run: run-migration.bat
echo.
echo Option C - Manual SQL:
echo   Copy and paste this SQL in MySQL Workbench:
echo.
echo   USE secucctv_db;  -- or cctv_shop
echo   
echo   ALTER TABLE products ADD COLUMN IF NOT EXISTS 
echo     image_url1 MEDIUMTEXT AFTER image_url;
echo   
echo   ALTER TABLE products ADD COLUMN IF NOT EXISTS 
echo     image_url2 MEDIUMTEXT AFTER image_url1;
echo   
echo   ALTER TABLE products ADD COLUMN IF NOT EXISTS 
echo     image_url3 MEDIUMTEXT AFTER image_url2;
echo   
echo   UPDATE products 
echo   SET image_url1 = image_url 
echo   WHERE image_url1 IS NULL AND image_url IS NOT NULL;
echo.
echo Press any key when migration is complete...
pause >nul
echo.

echo ===============================================
echo  STEP 3: Verify Database Columns
echo ===============================================
echo.
echo In MySQL Workbench, run this query:
echo.
echo   DESCRIBE products;
echo.
echo You should see these columns:
echo   - image_url      (MEDIUMTEXT)
echo   - image_url1     (MEDIUMTEXT)  ^<-- NEW
echo   - image_url2     (MEDIUMTEXT)  ^<-- NEW
echo   - image_url3     (MEDIUMTEXT)  ^<-- NEW
echo.
echo Press any key when verified...
pause >nul
echo.

echo ===============================================
echo  STEP 4: Start Backend Server
echo ===============================================
echo.
echo Opening new window to start backend...
echo Watch for "Started DemoApplication" message
echo.
start "Backend Server" cmd /k "cd /d %~dp0backend && mvnw spring-boot:run"
echo.
echo Waiting for backend to start...
timeout /t 15 /nobreak >nul
echo.

echo ===============================================
echo  STEP 5: Test Image Upload
echo ===============================================
echo.
echo 1. Go to: http://localhost:5173/admin
echo 2. Click "Add New Product"
echo 3. Fill in product details
echo 4. Upload 3 images:
echo    - Image 1: Browse and select first image
echo    - Image 2: Browse and select second image  
echo    - Image 3: Browse and select third image
echo 5. Click "Add Product"
echo 6. You should see "Product added successfully!"
echo.
echo Press any key to open Admin Panel in browser...
pause >nul
start http://localhost:5173/admin
echo.

echo ===============================================
echo  STEP 6: Verify in Database
echo ===============================================
echo.
echo In MySQL Workbench, run:
echo.
echo   SELECT id, name,
echo     LENGTH(image_url1) as img1_size,
echo     LENGTH(image_url2) as img2_size,
echo     LENGTH(image_url3) as img3_size
echo   FROM products
echo   ORDER BY id DESC
echo   LIMIT 1;
echo.
echo All 3 images should have size ^> 0
echo.
echo Press any key when verified...
pause >nul
echo.

echo ===============================================
echo  STEP 7: Test Slideshow
echo ===============================================
echo.
echo 1. Go to: http://localhost:5173/store
echo 2. Click on the product you just added
echo 3. You should see:
echo    - Image slideshow with 3 images
echo    - Left/Right arrow buttons
echo    - 3 dot indicators at bottom
echo    - Auto-slide every 3 seconds
echo.
start http://localhost:5173/store
echo.
echo ===============================================
echo  TROUBLESHOOTING
echo ===============================================
echo.
echo If images still don't save:
echo.
echo 1. Check backend console for errors
echo 2. Check browser console (F12) for errors
echo 3. Verify database columns exist
echo 4. Check MySQL service is running
echo 5. Verify correct database name in application.properties
echo.
echo Common Issues:
echo   - Wrong database name (secucctv_db vs cctv_shop)
echo   - MySQL password incorrect
echo   - Backend not running
echo   - Port 8080 already in use
echo.
echo ===============================================
echo.
echo Setup complete! Check if images are saving now.
echo.
pause
