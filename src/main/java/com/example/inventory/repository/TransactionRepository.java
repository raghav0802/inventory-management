package com.example.inventory.repository;

import com.example.inventory.model.Transaction;
import com.example.inventory.model.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    List<Transaction> findByProductId(String productId);
    List<Transaction> findByType(TransactionType type);
    List<Transaction> findByUserId(String userId);
}
