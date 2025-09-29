package com.example.inventory.controller;


import com.example.inventory.service.ProductSearchService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.example.inventory.model.ProductIndex;
import java.util.List;

@RestController
@RequestMapping("/api/search")
public class ProductSearchController {

    private final ProductSearchService searchService;

    public ProductSearchController(ProductSearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    public List<ProductIndex> search(@RequestParam String q) {
        return searchService.searchProducts(q);
    }
}

