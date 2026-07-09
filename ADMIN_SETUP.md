# Quick Setup: Create Admin User

## Method 1: Using API Endpoint (Easiest)

1. Make sure your backend is running on `http://localhost:8080`

2. Open a new PowerShell terminal and run:

```powershell
Invoke-RestMethod -Uri "http://localhost:8080/api/auth/setup-admin" -Method POST -ContentType "application/json"
```

This will create the admin user with:
- **Email:** admin@cctv.com
- **Password:** admin123

## Method 2: Using MySQL Command

If you have MySQL client installed:

```powershell
# Navigate to backend folder
cd 'd:\React Projects\cctv\backend'

# Run the SQL file (update path to mysql.exe if needed)
mysql -u root -p secucctv_db < insert_admin.sql
```

## Method 3: Using MySQL Workbench or phpMyAdmin

1. Open your MySQL management tool
2. Connect to your database
3. Select the `secucctv_db` database
4. Run this SQL query:

```sql
INSERT INTO users (full_name, email, password, role, created_at, is_active) 
VALUES ('Admin User', 'admin@cctv.com', 'admin123', 'ADMIN', NOW(), true);
```

## Verify Admin User Was Created

Run this in MySQL:

```sql
SELECT * FROM users WHERE email = 'admin@cctv.com';
```

## Then Login

1. Go to: `http://localhost:5173/admin/login`
2. Enter:
   - Email: `admin@cctv.com`
   - Password: `admin123`

---

**Note:** If you get "Email already registered" error from the API, it means the admin user already exists and you can login directly!
