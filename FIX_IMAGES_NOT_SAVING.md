# Fix: 3 Images Not Saving to Database

## Problem
When uploading 3 images in the Admin Panel, they are not being saved to the database.

## Root Cause
The database table `products` doesn't have the new columns (`image_url1`, `image_url2`, `image_url3`) yet.

## Solution - Step by Step

### Option 1: Automatic Setup (Recommended)

**Step 1: Run the migration script**
```bash
cd backend
mysql -u root -p cctv_shop < add_multiple_images.sql
```
Enter your MySQL root password when prompted.

**Step 2: Start the backend**
```bash
mvnw spring-boot:run
```

### Option 2: Manual Setup (If Option 1 fails)

**Step 1: Open MySQL Workbench or MySQL Command Line**

**Step 2: Run these SQL commands:**
```sql
USE cctv_shop;

-- Check if columns exist
SELECT COLUMN_NAME FROM INFORMATION_SCHEMA.COLUMNS 
WHERE TABLE_NAME = 'products' AND COLUMN_NAME LIKE 'image_url%';

-- Add columns if they don't exist
ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url1 MEDIUMTEXT AFTER image_url;
ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url2 MEDIUMTEXT AFTER image_url1;
ALTER TABLE products ADD COLUMN IF NOT EXISTS image_url3 MEDIUMTEXT AFTER image_url2;

-- Migrate existing data
UPDATE products 
SET image_url1 = image_url 
WHERE image_url IS NOT NULL AND image_url != '' 
  AND (image_url1 IS NULL OR image_url1 = '');

-- Verify columns exist
DESCRIBE products;
```

**Step 3: Restart Backend**
```bash
cd backend
mvnw spring-boot:run
```

### Option 3: Using MySQL Workbench GUI

1. Open MySQL Workbench
2. Connect to your database
3. Select `cctv_shop` database
4. Right-click on `products` table → "Alter Table"
5. Add three new columns:
   - Column Name: `image_url1`, Datatype: `MEDIUMTEXT`
   - Column Name: `image_url2`, Datatype: `MEDIUMTEXT`
   - Column Name: `image_url3`, Datatype: `MEDIUMTEXT`
6. Click "Apply"
7. Restart backend server

## Verification Steps

### 1. Check Database Columns
```sql
DESCRIBE products;
```

You should see:
```
image_url       MEDIUMTEXT
image_url1      MEDIUMTEXT
image_url2      MEDIUMTEXT
image_url3      MEDIUMTEXT
```

### 2. Test Image Upload

**A. Add a new product with 3 images:**
1. Go to Admin Panel: http://localhost:5173/admin
2. Click "Add New Product"
3. Fill in product details
4. Upload 3 different images (Image 1, Image 2, Image 3)
5. Click "Add Product"
6. You should see "Product added successfully!"

**B. Verify in Database:**
```sql
SELECT id, name, 
  LENGTH(image_url1) as img1_size,
  LENGTH(image_url2) as img2_size,
  LENGTH(image_url3) as img3_size
FROM products 
WHERE id = (SELECT MAX(id) FROM products);
```

**C. Check in Product Detail Page:**
1. Go to Store: http://localhost:5173/store
2. Click on the product you just added
3. You should see a slideshow with 3 images
4. Arrow buttons should work to navigate

### 3. Check API Response

1. Open browser DevTools (F12)
2. Go to Network tab
3. Navigate to product detail page
4. Find the API call to `/api/products/{id}`
5. Check response - should include:
```json
{
  "id": 1,
  "name": "Product Name",
  "imageUrl1": "data:image/jpeg;base64,...",
  "imageUrl2": "data:image/jpeg;base64,...",
  "imageUrl3": "data:image/jpeg;base64,...",
  "imageUrls": [
    "data:image/jpeg;base64,...",
    "data:image/jpeg;base64,...",
    "data:image/jpeg;base64,..."
  ]
}
```

## Common Issues and Solutions

### Issue 1: "Column already exists" error
**Solution:** This is normal if you ran the migration before. The script handles this gracefully.

### Issue 2: Backend won't start
**Solution:**
1. Check for Java compilation errors:
   ```bash
   cd backend
   mvnw clean compile
   ```
2. If errors appear, check the console output
3. Verify all imports in Product.java are correct

### Issue 3: Images upload but don't show in slideshow
**Solution:**
1. Clear browser cache (Ctrl+Shift+Delete)
2. Hard refresh (Ctrl+F5)
3. Check browser console for JavaScript errors
4. Verify API response includes `imageUrls` array

### Issue 4: Only 1 image shows instead of 3
**Solution:**
1. Check if all 3 images were uploaded successfully
2. Verify database has values in `image_url1`, `image_url2`, `image_url3`
3. Check ProductImageSlideshow component is imported correctly

## Files Modified

### Backend:
- ✅ `Product.java` - Added imageUrl1, imageUrl2, imageUrl3 fields
- ✅ `ProductController.java` - Accepts image1, image2, image3 parameters
- ✅ `add_multiple_images.sql` - Database migration script

### Frontend:
- ✅ `AdminPanel.jsx` - 3 image upload fields
- ✅ `ProductDetail.jsx` - Slideshow integration
- ✅ `ProductImageSlideshow.jsx` - Slideshow component

## Testing Checklist

- [ ] Database columns created successfully
- [ ] Backend starts without errors
- [ ] Can upload 3 images in Admin Panel
- [ ] Images save to database
- [ ] Product detail page shows slideshow
- [ ] Arrow buttons work
- [ ] Dot indicators show current image
- [ ] Works with 1, 2, or 3 images

## Quick Test Command

Run this in MySQL to quickly test:
```sql
-- Add a test product with 3 images
INSERT INTO products (name, brand, price, category, short_desc, image_url1, image_url2, image_url3)
VALUES 
  ('Test Camera', 'TestBrand', 1000.00, 'CCTV Camera', 'Test product', 
   'data:image/png;base64,iVBORw0KGgo...', 
   'data:image/png;base64,iVBORw0KGgo...', 
   'data:image/png;base64,iVBORw0KGgo...');

-- Verify it was added
SELECT id, name, 
  SUBSTRING(image_url1, 1, 50) as img1_preview,
  SUBSTRING(image_url2, 1, 50) as img2_preview,
  SUBSTRING(image_url3, 1, 50) as img3_preview
FROM products 
WHERE name = 'Test Camera';
```

## Need Help?

If issues persist:
1. Check backend console for error messages
2. Check browser console for frontend errors
3. Verify MySQL service is running
4. Ensure database name is `cctv_shop`
5. Check all files were saved correctly

---

**Status:** Ready to implement
**Last Updated:** Now
**Action Required:** Run database migration and restart backend
