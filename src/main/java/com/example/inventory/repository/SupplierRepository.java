package com.example.inventory.repository;

import com.example.inventory.model.Supplier;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SupplierRepository extends MongoRepository<Supplier, String> {
    Optional<Supplier> findByCompanyName(String companyName);
    Optional<Supplier> findByEmail(String email);
}
