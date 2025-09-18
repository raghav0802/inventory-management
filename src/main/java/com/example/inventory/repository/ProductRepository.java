package com.example.inventory.repository;

import com.example.inventory.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // Custom queries
    Optional<Product> findByCategory(String category);
    Optional<Product> findBySupplierId(String supplierId);
    Optional<Product> findByNameContainingIgnoreCase(String name);

    Optional<Product> findBySku(String sku);
}
