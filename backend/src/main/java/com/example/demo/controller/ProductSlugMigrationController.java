package com.example.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ProductSlugMigrationService;

@RestController
@RequestMapping("/api/products")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174", "https://secuengineering.netlify.app"})
public class ProductSlugMigrationController {

    @Autowired
    private ProductSlugMigrationService productSlugMigrationService;

    @PostMapping("/migrate-slugs")
    public ResponseEntity<String> migrateSlugs() {
        productSlugMigrationService.generateMissingSlugs();
        return ResponseEntity.ok("Slug migration completed");
    }
}
