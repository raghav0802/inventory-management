package com.example.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDate;

@Document(collection = "products")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {
    @Id
    private String id;
    private String name;
    private String description;
    private String category;
    private String sku; // Stock Keeping Unit (unique code)
    private Double price;
    private Integer quantity;

    private String supplierId; // Reference to Supplier document
    private LocalDate expiryDate; // Optional (null if not applicable)

    private LocalDate createdAt;
    private LocalDate updatedAt;
}