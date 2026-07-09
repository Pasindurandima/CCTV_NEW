# Category Management System

## Overview
Complete customizable category management system where admins can add, edit, delete, and organize product categories. Categories are displayed dynamically throughout the system.

## Features
✅ **Admin Category Management** - Full CRUD operations for categories
✅ **Dynamic Category Dropdown** - AdminPanel product form fetches categories from API
✅ **Store Sidebar Filtering** - Customers can filter products by category
✅ **Display Order** - Control the order categories appear
✅ **Active/Inactive Status** - Enable/disable categories without deleting
✅ **Database-Driven** - All categories stored in MySQL database

## Backend Implementation

### 1. Category Entity (`Category.java`)
```java
@Entity
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String name;
    
    @Column(length = 500)
    private String description;
    
    @Column(nullable = false)
    private Integer displayOrder = 0;
    
    @Column(nullable = false)
    private Boolean isActive = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(nullable = false)
    private LocalDateTime updatedAt;
}
```

### 2. Category Repository (`CategoryRepository.java`)
- `findAllByIsActiveTrueOrderByDisplayOrderAsc()` - Get active categories for public store
- `findAllByOrderByDisplayOrderAsc()` - Get all categories for admin
- `findByName(String name)` - Find category by name
- `existsByName(String name)` - Check if category exists

### 3. Category Controller (`CategoryController.java`)
**Base URL**: `http://localhost:8080/api/categories`

#### Endpoints:
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/active` | Get all active categories (public) |
| GET | `/` | Get all categories (admin) |
| GET | `/{id}` | Get category by ID |
| POST | `/` | Create new category |
| PUT | `/{id}` | Update category |
| DELETE | `/{id}` | Delete category |
| PATCH | `/{id}/toggle` | Toggle active status |

#### Example API Calls:

**Create Category**:
```bash
POST http://localhost:8080/api/categories
Content-Type: application/json

{
  "name": "Smart Locks",
  "description": "Electronic and smart door locks",
  "displayOrder": 11,
  "isActive": true
}
```

**Get Active Categories**:
```bash
GET http://localhost:8080/api/categories/active
```

**Update Category**:
```bash
PUT http://localhost:8080/api/categories/1
Content-Type: application/json

{
  "name": "Wireless Camera Systems",
  "description": "Updated description",
  "displayOrder": 1,
  "isActive": true
}
```

## Frontend Implementation

### 1. Admin Category Management Page (`AdminCategories.jsx`)
**Route**: `/admin/categories`

Features:
- ➕ Add new categories
- ✏️ Edit existing categories
- 🗑️ Delete categories
- 🟢/🔴 Toggle active/inactive status
- 📊 View in table format with display order
- 🔍 Shows creation date and status

### 2. AdminPanel Product Form
**Route**: `/admin/products`

- Category dropdown now fetches from API: `GET /api/categories/active`
- Shows warning if no categories exist
- Dynamic options based on database

Before:
```jsx
<option value="Wireless Camera">Wireless Camera</option>
<option value="Wired Camera">Wired Camera</option>
// ... hardcoded
```

After:
```jsx
{categories.map((cat) => (
  <option key={cat.id} value={cat.name}>
    {cat.name}
  </option>
))}
```

### 3. Store Sidebar Filtering (`Store.jsx`)
**Route**: `/store`

- Sidebar fetches categories from API: `GET /api/categories/active`
- Displays "All Products" button + dynamic category buttons
- Filters products when category clicked
- Shows product count per filter

```jsx
{categories.map((category) => (
  <button
    key={category.id}
    onClick={() => setSelectedCategory(category.name)}
  >
    {category.name}
  </button>
))}
```

## Database Setup

### Run SQL Migration:
```bash
mysql -u root -p secucctv_db < backend/create_categories.sql
```

### Default Categories:
1. Wireless Camera
2. IP Camera
3. Analog CCTV
4. DVR
5. NVR
6. CCTV Package
7. Hard Drive Memory
8. Cameras
9. Mobile Accessories
10. Power Bank

## Usage Workflow

### Admin Workflow:
1. **Navigate to Categories**: `/admin/categories`
2. **Add Category**: Click "➕ Add New Category"
3. **Fill Form**:
   - Name (required, unique)
   - Description (optional)
   - Display Order (controls sidebar order)
   - Active checkbox (show/hide in store)
4. **Save**: Category appears in system

### Adding Product with Category:
1. **Navigate to Products**: `/admin/products`
2. **Select Category**: Dropdown shows all active categories
3. **Add Product**: Category is stored with product

### Customer Experience:
1. **Visit Store**: `/store`
2. **See Sidebar**: All active categories listed
3. **Click Category**: Products filter automatically
4. **Search**: Works across categories and products

## Files Created/Modified

### Backend:
- ✅ `backend/src/main/java/com/example/demo/entity/Category.java`
- ✅ `backend/src/main/java/com/example/demo/repository/CategoryRepository.java`
- ✅ `backend/src/main/java/com/example/demo/controller/CategoryController.java`
- ✅ `backend/create_categories.sql`

### Frontend:
- ✅ `frontend/src/pages/AdminCategories.jsx`
- ✅ `frontend/src/styles/AdminCategories.css`
- ✅ `frontend/src/pages/AdminPanel.jsx` (updated)
- ✅ `frontend/src/pages/Store.jsx` (updated)
- ✅ `frontend/src/components/AdminNavbar.jsx` (updated)
- ✅ `frontend/src/App.jsx` (updated)

## Testing

### 1. Test Backend:
```bash
# Start backend
cd backend
./mvnw spring-boot:run

# Test endpoints
curl http://localhost:8080/api/categories/active
```

### 2. Test Frontend:
```bash
# Start frontend
cd frontend
npm run dev

# Navigate to:
# - http://localhost:5174/admin/categories (manage)
# - http://localhost:5174/admin/products (use in form)
# - http://localhost:5174/store (filter products)
```

### 3. Test Database:
```sql
USE secucctv_db;
SELECT * FROM categories ORDER BY display_order;
```

## Category System Benefits

1. **Industrial-Grade** ✅
   - Real production system approach
   - Database-driven, not hardcoded
   - Scalable and maintainable

2. **Flexible Management** ✅
   - Admin controls all categories
   - Add/remove categories without code changes
   - Reorder categories by display order

3. **Better UX** ✅
   - Customers filter products easily
   - Organized product browsing
   - Sidebar navigation

4. **Future-Proof** ✅
   - Easy to add category images
   - Can add subcategories later
   - Can track category analytics

## API Response Examples

### GET /api/categories/active
```json
[
  {
    "id": 1,
    "name": "Wireless Camera",
    "description": "WiFi and battery-powered security cameras",
    "displayOrder": 1,
    "isActive": true,
    "createdAt": "2024-01-21T10:00:00",
    "updatedAt": "2024-01-21T10:00:00"
  },
  {
    "id": 2,
    "name": "IP Camera",
    "description": "Network-based high-resolution security cameras",
    "displayOrder": 2,
    "isActive": true,
    "createdAt": "2024-01-21T10:00:00",
    "updatedAt": "2024-01-21T10:00:00"
  }
]
```

## Troubleshooting

### Category dropdown empty in AdminPanel:
- Check backend is running on port 8080
- Verify categories exist: `SELECT * FROM categories WHERE is_active = 1;`
- Check browser console for API errors

### Store sidebar not showing categories:
- Verify `/api/categories/active` returns data
- Check categories have `isActive = true`
- Ensure frontend is making API call correctly

### Category already exists error:
- Category names must be unique
- Check existing categories: `SELECT name FROM categories;`
- Edit existing category instead of creating new one

## Next Steps

Potential enhancements:
- 📷 Add category images/icons
- 📊 Category-wise sales analytics
- 🏷️ Multiple categories per product (tags)
- 📱 Category description on hover
- 🔍 Category SEO optimization

---

**Status**: ✅ Fully Implemented and Tested
**Last Updated**: 2024-01-21
