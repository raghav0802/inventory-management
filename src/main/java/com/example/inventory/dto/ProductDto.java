package com.example.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductDto {
    private String id;
    private String name;
    private String description;
    private String category;
    private String sku;
    private double price;
    private int quantity;
    private String supplierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
