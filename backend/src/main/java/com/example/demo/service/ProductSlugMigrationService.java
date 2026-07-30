package com.example.demo.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.util.SlugUtils;

import jakarta.annotation.PostConstruct;

@Service
public class ProductSlugMigrationService {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void initializeMissingSlugs() {
        generateMissingSlugs();
    }

    public void generateMissingSlugs() {
        List<Product> products = productRepository.findAll();
        for (Product product : products) {
            if (product.getSlug() == null || product.getSlug().trim().isEmpty()) {
                String slug = SlugUtils.createUniqueSlug(product.getName(), candidate -> {
                    Optional<Product> existing = productRepository.findBySlug(candidate);
                    return existing.isPresent() && !existing.get().getId().equals(product.getId());
                });
                product.setSlug(slug);
                productRepository.save(product);
            }
        }
    }
}
