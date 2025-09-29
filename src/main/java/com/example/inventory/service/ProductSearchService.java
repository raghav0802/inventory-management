package com.example.inventory.service;

import com.example.inventory.model.Product;
import com.example.inventory.model.ProductIndex;
import com.example.inventory.repository.ProductSearchRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductSearchService {

    private final ProductSearchRepository searchRepository;

    public ProductSearchService(ProductSearchRepository searchRepository) {
        this.searchRepository = searchRepository;
    }

    public void indexProduct(Product product) {
        ProductIndex index = new ProductIndex();
        index.setId(product.getId());
        index.setName(product.getName());
        index.setCategory(product.getCategory());
        index.setPrice(product.getPrice());
        index.setDescription(product.getDescription());
        searchRepository.save(index);
    }

    public void deleteFromIndex(String productId) {
        searchRepository.deleteById(productId);
    }

    public List<ProductIndex> searchProducts(String keyword) {
        return searchRepository.findByNameContainingOrCategoryContaining(keyword, keyword);
    }
}
