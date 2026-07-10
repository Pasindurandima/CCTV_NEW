@echo off
echo Running database migration to add multiple image columns...
mysql -h mysql-36042108-pasirandima2001-745a.c.aivencloud.com -P 28247 -u avnadmin -pAVNS_0t-CImdsGZPopmBRpRW --ssl-mode=REQUIRED defaultdb < add_multiple_images.sql
if %ERRORLEVEL% EQU 0 (
    echo Migration completed successfully!
) else (
    echo Migration failed. Please check the error above.
)
pause
