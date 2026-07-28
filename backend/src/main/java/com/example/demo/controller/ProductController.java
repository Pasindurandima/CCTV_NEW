package com.example.demo.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.dto.ErrorResponse;
import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "https://secuengineering.netlify.app"})
public class ProductController {

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductRepository productRepository;

    // Get all products
    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        try {
            List<Product> products = productRepository.findAll();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get product by ID
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable("id") Long id) {
        Optional<Product> productData = productRepository.findById(id);
        return productData.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Get products by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable("category") String category) {
        try {
            List<Product> products = productRepository.findByCategory(category);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Get products by brand
    @GetMapping("/brand/{brand}")
    public ResponseEntity<List<Product>> getProductsByBrand(@PathVariable("brand") String brand) {
        try {
            List<Product> products = productRepository.findByBrand(brand);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Search products by name
    @GetMapping("/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        try {
            List<Product> products = productRepository.findByNameContainingIgnoreCase(name);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // Create new product with file upload
    @PostMapping
    public ResponseEntity<?> createProduct(
            @RequestParam String name,
            @RequestParam String brand,
            @RequestParam Double price,
            @RequestParam(required = false) Double originalPrice,
            @RequestParam String category,
            @RequestParam String shortDesc,
            @RequestParam(required = false) String features,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile image1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) MultipartFile image3) {
        try {
            // Validation
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Product name is required"));
            }
            if (brand == null || brand.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Brand is required"));
            }
            if (price == null || price <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Valid price is required"));
            }
            if (category == null || category.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Category is required"));
            }

            Product product = new Product();
            product.setName(name);
            product.setBrand(brand);
            product.setPrice(price);
            product.setOriginalPrice(originalPrice);
            product.setCategory(category);
            product.setShortDesc(shortDesc);
            
            // Parse features from JSON array string
            if (features != null && !features.isEmpty()) {
                try {
                    // Remove brackets and parse as simple list
                    String cleanFeatures = features.replaceAll("[\\[\\]\"']", "");
                    List<String> featureList = Arrays.stream(cleanFeatures.split(","))
                        .map(String::trim)
                        .filter(f -> !f.isEmpty())
                        .collect(Collectors.toCollection(ArrayList::new));
                    product.setFeatures(featureList);
                } catch (Exception e) {
                    product.setFeatures(new ArrayList<>());
                }
            }
            
            // Handle legacy single image upload
            if (image != null && !image.isEmpty()) {
                try {
                    byte[] imageBytes = image.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    product.setImageUrl(dataUri);
                    // Also set as first image if image1 not provided
                    if (image1 == null || image1.isEmpty()) {
                        product.setImageUrl1(dataUri);
                    }
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image: " + e.getMessage()));
                }
            }
            
            // Handle image1 upload
            if (image1 != null && !image1.isEmpty()) {
                try {
                    byte[] imageBytes = image1.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image1.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    product.setImageUrl1(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image1: " + e.getMessage()));
                }
            }
            
            // Handle image2 upload
            if (image2 != null && !image2.isEmpty()) {
                try {
                    byte[] imageBytes = image2.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image2.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    product.setImageUrl2(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image2: " + e.getMessage()));
                }
            }
            
            // Handle image3 upload
            if (image3 != null && !image3.isEmpty()) {
                try {
                    byte[] imageBytes = image3.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image3.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    product.setImageUrl3(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image3: " + e.getMessage()));
                }
            }

            Product newProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to save product: " + e.getMessage()));
        }
    }

    // Backup: Create new product with JSON (for API compatibility)
    @PostMapping(consumes = "application/json")
    public ResponseEntity<?> createProductJson(@RequestBody Product product) {
        try {
            // Validation
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Product name is required"));
            }
            if (product.getBrand() == null || product.getBrand().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Brand is required"));
            }
            if (product.getPrice() == null || product.getPrice() <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Valid price is required"));
            }
            if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Category is required"));
            }

            Product newProduct = productRepository.save(product);
            return ResponseEntity.status(HttpStatus.CREATED).body(newProduct);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to save product: " + e.getMessage()));
        }
    }

    // Update product with file upload
    @PostMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProductMultipart(
            @PathVariable("id") Long id,
            @RequestParam String name,
            @RequestParam String brand,
            @RequestParam Double price,
            @RequestParam(required = false) Double originalPrice,
            @RequestParam String category,
            @RequestParam String shortDesc,
            @RequestParam(required = false) String features,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile image1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) MultipartFile image3) {
        return updateProduct(id, name, brand, price, originalPrice, category, shortDesc, features, image, image1, image2, image3);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable("id") Long id,
            @RequestParam String name,
            @RequestParam String brand,
            @RequestParam Double price,
            @RequestParam(required = false) Double originalPrice,
            @RequestParam String category,
            @RequestParam String shortDesc,
            @RequestParam(required = false) String features,
            @RequestParam(required = false) MultipartFile image,
            @RequestParam(required = false) MultipartFile image1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) MultipartFile image3) {
        try {
            Optional<Product> productData = productRepository.findById(id);

            if (productData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Product not found with id: " + id));
            }

            // Validation
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Product name is required"));
            }
            if (brand == null || brand.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Brand is required"));
            }
            if (price == null || price <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Valid price is required"));
            }
            if (category == null || category.trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Category is required"));
            }

            Product existingProduct = productData.get();
            existingProduct.setName(name);
            existingProduct.setBrand(brand);
            existingProduct.setPrice(price);
            existingProduct.setOriginalPrice(originalPrice);
            existingProduct.setCategory(category);
            existingProduct.setShortDesc(shortDesc);
            
            // Parse features from JSON array string
            if (features != null && !features.isEmpty()) {
                try {
                    // Remove brackets and parse as simple list
                    String cleanFeatures = features.replaceAll("[\\[\\]\"']", "");
                    List<String> featureList = Arrays.stream(cleanFeatures.split(","))
                        .map(String::trim)
                        .filter(f -> !f.isEmpty())
                        .collect(Collectors.toCollection(ArrayList::new));
                    existingProduct.setFeatures(featureList);
                } catch (Exception e) {
                    existingProduct.setFeatures(new ArrayList<>());
                }
            }
            
            // Handle legacy image upload - only update if new image is provided
            if (image != null && !image.isEmpty()) {
                try {
                    byte[] imageBytes = image.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    existingProduct.setImageUrl(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image: " + e.getMessage()));
                }
            }
            
            // Handle image1 upload
            if (image1 != null && !image1.isEmpty()) {
                try {
                    byte[] imageBytes = image1.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image1.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    existingProduct.setImageUrl1(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image1: " + e.getMessage()));
                }
            }
            
            // Handle image2 upload
            if (image2 != null && !image2.isEmpty()) {
                try {
                    byte[] imageBytes = image2.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image2.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    existingProduct.setImageUrl2(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image2: " + e.getMessage()));
                }
            }
            
            // Handle image3 upload
            if (image3 != null && !image3.isEmpty()) {
                try {
                    byte[] imageBytes = image3.getBytes();
                    String base64Image = Base64.getEncoder().encodeToString(imageBytes);
                    String mimeType = image3.getContentType();
                    String dataUri = "data:" + mimeType + ";base64," + base64Image;
                    existingProduct.setImageUrl3(dataUri);
                } catch (Exception e) {
                    return ResponseEntity.badRequest()
                            .body(new ErrorResponse("Failed to process image3: " + e.getMessage()));
                }
            }

            return ResponseEntity.ok(productRepository.save(existingProduct));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.toString();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to update product", errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Backup: Update product with JSON (for API compatibility)
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateProductJson(@PathVariable("id") Long id, @RequestBody Product product) {
        try {
            Optional<Product> productData = productRepository.findById(id);

            if (productData.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("Product not found with id: " + id));
            }

            // Validation
            if (product.getName() == null || product.getName().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Product name is required"));
            }
            if (product.getBrand() == null || product.getBrand().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Brand is required"));
            }
            if (product.getPrice() == null || product.getPrice() <= 0) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Valid price is required"));
            }
            if (product.getCategory() == null || product.getCategory().trim().isEmpty()) {
                return ResponseEntity.badRequest()
                        .body(new ErrorResponse("Category is required"));
            }

            Product existingProduct = productData.get();
            existingProduct.setName(product.getName());
            existingProduct.setBrand(product.getBrand());
            existingProduct.setPrice(product.getPrice());
            existingProduct.setOriginalPrice(product.getOriginalPrice());
            existingProduct.setCategory(product.getCategory());
            existingProduct.setShortDesc(product.getShortDesc());
            if (product.getFeatures() != null) {
                existingProduct.setFeatures(new ArrayList<>(product.getFeatures()));
            }

            if (product.getImageUrl() != null && !product.getImageUrl().isEmpty()) {
                existingProduct.setImageUrl(product.getImageUrl());
            }
            if (product.getImageUrl1() != null && !product.getImageUrl1().isEmpty()) {
                existingProduct.setImageUrl1(product.getImageUrl1());
            }
            if (product.getImageUrl2() != null && !product.getImageUrl2().isEmpty()) {
                existingProduct.setImageUrl2(product.getImageUrl2());
            }
            if (product.getImageUrl3() != null && !product.getImageUrl3().isEmpty()) {
                existingProduct.setImageUrl3(product.getImageUrl3());
            }
            if (product.getFeatures() != null) {
                existingProduct.setFeatures(new ArrayList<>(product.getFeatures()));
            }

            return ResponseEntity.ok(productRepository.save(existingProduct));
        } catch (Exception e) {
            e.printStackTrace();
            String errorMessage = e.getMessage() != null ? e.getMessage() : e.toString();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Failed to update product", errorMessage, HttpStatus.INTERNAL_SERVER_ERROR.value()));
        }
    }

    // Delete product
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id) {
        try {
            productRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Delete all products
    @DeleteMapping
    public ResponseEntity<Void> deleteAllProducts() {
        try {
            productRepository.deleteAll();
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}