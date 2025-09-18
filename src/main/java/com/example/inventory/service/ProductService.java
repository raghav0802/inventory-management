package com.example.inventory.service;

import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // Get all products
    public List<Product> getAll() {
        return repo.findAll();
    }

    // Get single product
    public Optional<Product> getById(String id) {
        return repo.findById(id);
    }

    // Add new product
    public Product create(Product product) {
        product.setCreatedAt(LocalDate.now());
        product.setUpdatedAt(LocalDate.now());
        return repo.save(product);
    }

    // Update product
    public Product update(String id, Product updated) {
        return repo.findById(id).map(existing -> {
            existing.setName(updated.getName());
            existing.setDescription(updated.getDescription());
            existing.setCategory(updated.getCategory());
            existing.setPrice(updated.getPrice());
            existing.setQuantity(updated.getQuantity());
            existing.setSupplierId(updated.getSupplierId());
            existing.setExpiryDate(updated.getExpiryDate());
            existing.setUpdatedAt(LocalDate.now());
            return repo.save(existing);
        }).orElseThrow(() -> new RuntimeException("Product not found"));
    }

    // Delete product
    public void delete(String id) {
        repo.deleteById(id);
    }
}
