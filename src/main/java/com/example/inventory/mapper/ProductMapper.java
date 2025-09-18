package com.example.inventory.mapper;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.model.Product;

import java.time.LocalDate;

public class ProductMapper {
    public static ProductDto toDto(Product product) {
        if (product == null) return null;
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setCategory(product.getCategory());
        dto.setSku(product.getSku());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        dto.setSupplierId(product.getSupplierId());
        return dto;
    }

    public static Product toEntity(ProductDto dto) {
        if (dto == null) return null;

        return Product.builder()
                .id(dto.getId())
                .name(dto.getName())
                .description(dto.getDescription())
                .category(dto.getCategory())
                .sku(dto.getSku())
                .price(dto.getPrice())
                .quantity(dto.getQuantity())
                .supplierId(dto.getSupplierId())
                .createdAt(LocalDate.from(dto.getCreatedAt()))
                .updatedAt(LocalDate.from(dto.getUpdatedAt()))
                .build();
    }
}
