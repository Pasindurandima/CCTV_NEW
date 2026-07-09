# Admin Access Instructions

## Admin Login

Admin users can access the admin panel by navigating directly to the admin login URL:

**Admin Login URL:** `http://localhost:5173/admin/login`

## Default Admin Credentials

After setting up the database, use these credentials to login:

- **Email:** `admin@cctv.com`
- **Password:** `admin123`

## Important Notes

1. The admin login link is NOT visible in the user navigation bar
2. Regular users cannot access admin features
3. Admin must navigate directly to `/admin/login` URL
4. After successful login, admin will be redirected to the admin dashboard

## Admin Features

Once logged in as admin, you can access:
- **Dashboard** - Overview and statistics
- **Products** - Add, edit, delete products
- **Sales** - View and manage orders
- **Analytics** - View business analytics

## Security

⚠️ **Important:** Change the default admin password in production!

To add more admin users, insert records directly into the database:

```sql
INSERT INTO users (full_name, email, password, role, created_at, is_active) 
VALUES ('New Admin', 'newadmin@cctv.com', 'securepassword', 'ADMIN', NOW(), true);
```

---

**Note:** Regular users can browse and shop without any login. Only admin requires authentication.
