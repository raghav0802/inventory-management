package com.example.inventory.repository;

import com.example.inventory.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    Supplier findByCompanyName(String companyName);
    Supplier findByEmail(String email);
}
