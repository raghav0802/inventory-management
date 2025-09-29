package com.example.inventory.repository;

import com.example.inventory.model.ProductIndex;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import java.util.List;

public interface ProductSearchRepository extends ElasticsearchRepository<ProductIndex, String> {
    List<ProductIndex> findByNameContainingOrCategoryContaining(String name, String category);
}
