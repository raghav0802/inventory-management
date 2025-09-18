package com.example.inventory.repository;

import com.example.inventory.model.Transaction;
import com.example.inventory.model.TransactionType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends MongoRepository<Transaction, String> {
    Optional<Transaction> findByProductId(String productId);
    Optional<Transaction> findByType(TransactionType type);
    Optional<Transaction> findByUserId(String userId);
    List<Transaction> findByUserIdAndDateBetween(String userId, LocalDateTime from, LocalDateTime to);
}
