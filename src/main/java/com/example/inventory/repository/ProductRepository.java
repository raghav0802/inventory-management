package com.example.inventory.repository;

import com.example.inventory.model.Product;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ProductRepository extends MongoRepository<Product, String> {
    // Custom queries
    List<Product> findByCategory(String category);
    List<Product> findBySupplierId(String supplierId);
    List<Product> findByNameContainingIgnoreCase(String name);
}
