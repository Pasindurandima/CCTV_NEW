# CCTV E-Commerce Application Setup Guide

## Overview
This application consists of:
- **Backend**: Java Spring Boot REST API (Port 8080)
- **Frontend**: React Application (Port 5173)
- **Database**: MySQL (secucctv_db)

## Setup Instructions

### 1. Database Configuration

Make sure your MySQL server is running and you have created the database:

```sql
CREATE DATABASE secucctv_db;
```

Update database credentials in `backend/src/main/resources/application.properties`:
```properties
spring.datasource.username=root
spring.datasource.password=your_password_here
```

### 2. Start Backend (Spring Boot)

Navigate to the backend folder and run:

```bash
cd backend
./mvnw spring-boot:run
# On Windows: mvnw.cmd spring-boot:run
```

The backend will start on http://localhost:8080

### 3. Start Frontend (React)

Navigate to the frontend folder and run:

```bash
cd frontend
npm install  # First time only
npm run dev
```

The frontend will start on http://localhost:5173

## Features

### Admin Panel
- Access at: http://localhost:5173/admin
- Add new products with:
  - Name, Brand, Price, Original Price
  - Category selection
  - Short description
  - Multiple features
  - Image URL (optional)
- Edit existing products
- Delete products
- View all products

### Store Page
- Browse all products
- Filter by category
- Search products by name, brand, or category
- View product details
- Products are fetched from database in real-time

### API Endpoints

**Products API** (http://localhost:8080/api/products)
- `GET /api/products` - Get all products
- `GET /api/products/{id}` - Get product by ID
- `GET /api/products/category/{category}` - Filter by category
- `GET /api/products/brand/{brand}` - Filter by brand
- `GET /api/products/search?name={name}` - Search by name
- `POST /api/products` - Create new product
- `PUT /api/products/{id}` - Update product
- `DELETE /api/products/{id}` - Delete product

## Database Tables

### products
- id (BIGINT, Primary Key, Auto Increment)
- name (VARCHAR 255, NOT NULL)
- brand (VARCHAR 255, NOT NULL)
- price (DOUBLE, NOT NULL)
- original_price (DOUBLE, Nullable)
- category (VARCHAR 255, NOT NULL)
- short_desc (VARCHAR 500)
- image_url (VARCHAR 255)

### product_features
- product_id (BIGINT, Foreign Key → products.id)
- feature (VARCHAR 500)

## Categories Available
- Wireless Camera
- Analog CCTV
- IP Camera
- CCTV Package
- DVR
- NVR
- Hard Drive Memory
- Cameras
- Mobile Accessories
- Sound Devices
- TV and Monitor
- UPS Inverters
- Power Bank

## Next Steps

1. **Start both servers** (Backend on 8080, Frontend on 5173)
2. **Access Admin Panel** at http://localhost:5173/admin
3. **Add products** using the admin interface
4. **View products** in the store at http://localhost:5173/store

## Troubleshooting

### Backend won't start
- Check if MySQL is running
- Verify database credentials in application.properties
- Ensure database `secucctv_db` exists

### Frontend shows "Error Loading Products"
- Make sure backend is running on port 8080
- Check browser console for error details
- Verify CORS is configured correctly

### Database tables not created
- Check if `spring.jpa.hibernate.ddl-auto=update` is set in application.properties
- Tables are auto-created on first run
- Check backend console for errors

## Important Notes

- The hardcoded product data has been removed
- All products now come from the database
- Use the Admin Panel to manage products
- Database password in application.properties is empty by default - update if needed
