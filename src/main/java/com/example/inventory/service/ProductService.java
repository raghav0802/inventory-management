package com.example.inventory.service;

import com.example.inventory.dto.ProductDto;
import com.example.inventory.mapper.ProductMapper;
import com.example.inventory.model.Product;
import com.example.inventory.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {
    private final ProductRepository repo;

    public ProductService(ProductRepository repo) {
        this.repo = repo;
    }

    // Get all products
    public List<ProductDto> getAllProducts() {
        return repo.findAll()
                .stream()
                .map(ProductMapper::toDto)
                .collect(Collectors.toList());
    }

    // Get single product
    public Optional<ProductDto> getProductById(String id) {
        return repo.findById(id).map(ProductMapper::toDto);
    }

    // Add new product

    public ProductDto createProduct(ProductDto productDto) {


        Product product = ProductMapper.toEntity(productDto);

        LocalDateTime now = LocalDateTime.now();
        product.setCreatedAt(now);
        product.setUpdatedAt(now);

        Product saved = repo.save(product);
        return ProductMapper.toDto(saved);
    }

    // Update product

    public Optional<ProductDto> updateProduct(String id, ProductDto productDto) {
        return repo.findById(id).map(existing -> {
            existing.setName(productDto.getName());
            existing.setDescription(productDto.getDescription());
            existing.setCategory(productDto.getCategory());
            existing.setSku(productDto.getSku());
            existing.setPrice(productDto.getPrice());
            existing.setQuantity(productDto.getQuantity());
            existing.setSupplierId(productDto.getSupplierId());
            existing.setUpdatedAt(LocalDateTime.now());
            Product updated = repo.save(existing);
            return ProductMapper.toDto(updated);
        });
    }
    // Delete product
    public boolean deleteProduct(String id) {
        if (repo.existsById(id)) {
            repo.deleteById(id);
            return true;
        }
        return false;
    }
    public Optional<ProductDto> getProductBySku(String sku) {
        return repo.findBySku(sku).map(ProductMapper::toDto);
    }
}
