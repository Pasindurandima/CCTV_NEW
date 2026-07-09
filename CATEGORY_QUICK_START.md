# Quick Start Guide - Category Management System

## 🚀 Getting Started

### 1. Setup Database (First Time Only)
```bash
# Open MySQL
mysql -u root -p

# Run the category setup script
USE secucctv_db;
source D:/React\ Projects/cctv/backend/create_categories.sql
```

### 2. Start Backend
```bash
cd backend
./mvnw spring-boot:run
```
Backend will run on: `http://localhost:8080`

### 3. Start Frontend
```bash
cd frontend
npm run dev
```
Frontend will run on: `http://localhost:5174`

## 📂 How to Use Category Management

### Admin: Manage Categories
1. **Login**: Go to `http://localhost:5174/admin/login`
2. **Navigate**: Click "📂 Categories" in admin navbar
3. **Add Category**:
   - Click "➕ Add New Category"
   - Enter category name (e.g., "Smart Locks")
   - Add description (optional)
   - Set display order (controls sidebar position)
   - Check "Active" to show in store
   - Click "Add Category"

4. **Edit Category**:
   - Click ✏️ edit button on any category
   - Update fields and save

5. **Toggle Active/Inactive**:
   - Click 🟢/🔴 button to enable/disable category
   - Inactive categories won't show in store sidebar

6. **Delete Category**:
   - Click 🗑️ delete button
   - Confirm deletion

### Admin: Add Products with Categories
1. **Navigate**: Go to `http://localhost:5174/admin/products`
2. **Category Dropdown**: Now shows dynamic categories from database
3. **Select Category**: Choose from dropdown (only active categories shown)
4. **Add Product**: Fill form and save

### Customer: Filter by Category
1. **Visit Store**: Go to `http://localhost:5174/store`
2. **Sidebar**: See all active categories listed
3. **Click Category**: Products filter automatically
4. **Click "All Products"**: Show everything again

## 🔍 Testing the System

### Test 1: Add a New Category
```
1. Go to: http://localhost:5174/admin/categories
2. Click "➕ Add New Category"
3. Name: "Video Doorbells"
4. Description: "Smart video doorbell systems"
5. Display Order: 11
6. Active: ✓
7. Click "Add Category"
```

**Expected**: Category appears in table, shows in AdminPanel dropdown, shows in Store sidebar

### Test 2: Filter Products by Category
```
1. Add some products with different categories in AdminPanel
2. Go to: http://localhost:5174/store
3. Click "Wireless Camera" in sidebar
4. Only wireless cameras should display
5. Click "All Products" to reset
```

**Expected**: Products filter correctly, count updates

### Test 3: Inactive Category
```
1. Go to: http://localhost:5174/admin/categories
2. Click 🟢 toggle button on a category
3. Status changes to "✗ Inactive"
4. Go to Store sidebar
```

**Expected**: Category no longer appears in store sidebar

## 📊 Default Categories Installed

After running `create_categories.sql`, you'll have:

| Order | Category Name | Description |
|-------|--------------|-------------|
| 1 | Wireless Camera | WiFi and battery-powered security cameras |
| 2 | IP Camera | Network-based high-resolution cameras |
| 3 | Analog CCTV | Traditional analog CCTV systems |
| 4 | DVR | Digital Video Recorders |
| 5 | NVR | Network Video Recorders |
| 6 | CCTV Package | Complete CCTV system packages |
| 7 | Hard Drive Memory | Storage solutions |
| 8 | Cameras | General purpose cameras |
| 9 | Mobile Accessories | Cables and accessories |
| 10 | Power Bank | Portable power banks |

## 🛠️ API Endpoints

### Get Active Categories (for Store)
```bash
GET http://localhost:8080/api/categories/active
```

### Get All Categories (for Admin)
```bash
GET http://localhost:8080/api/categories
```

### Create Category
```bash
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "Smart Locks",
  "description": "Electronic door locks",
  "displayOrder": 11,
  "isActive": true
}
```

## ❓ Troubleshooting

### Problem: Category dropdown empty in AdminPanel
**Solution**: 
1. Check backend is running: `http://localhost:8080/api/categories/active`
2. Verify categories exist in database: `SELECT * FROM categories;`
3. Check browser console for errors

### Problem: Store sidebar shows no categories
**Solution**:
1. Ensure you ran `create_categories.sql`
2. Check categories are active: `SELECT * FROM categories WHERE is_active = 1;`
3. Verify API returns data: `http://localhost:8080/api/categories/active`

### Problem: "Category already exists" error
**Solution**: 
- Category names must be unique
- Edit the existing category instead of creating a new one
- Or use a different name

## 📝 Key Features

✅ **Fully Dynamic** - No hardcoded categories
✅ **Admin Control** - Add/edit/delete categories anytime
✅ **Display Order** - Control category order in sidebar
✅ **Active/Inactive** - Hide categories without deleting
✅ **Real-time Updates** - Changes reflect immediately
✅ **Industrial Grade** - Production-ready implementation

## 🎯 Next Steps

1. **Add more categories** as needed for your business
2. **Organize display order** to prioritize important categories
3. **Add products** using the new categories
4. **Monitor usage** to see which categories are popular

---

**Need Help?** Check [CATEGORY_SYSTEM.md](CATEGORY_SYSTEM.md) for detailed documentation.
