package com.example.inventory.service;

import com.example.inventory.model.*;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final ProductRepository productRepo;

    public TransactionService(TransactionRepository transactionRepo, ProductRepository productRepo) {
        this.transactionRepo = transactionRepo;
        this.productRepo = productRepo;
    }

    public List<Transaction> getAll() {
        return transactionRepo.findAll();
    }

    public List<Transaction> getByProductId(String productId) {
        return transactionRepo.findByProductId(productId);
    }

    // Handle stock IN (add quantity)
    public Transaction stockIn(String productId, int qty, String ref, String remarks, String userId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        product.setQuantity(product.getQuantity() + qty);
        productRepo.save(product);

        Transaction tx = Transaction.builder()
                .productId(productId)
                .type(TransactionType.IN)
                .quantity(qty)
                .referenceNumber(ref)
                .remarks(remarks)
                .userId(userId)
                .date(LocalDateTime.now())
                .build();

        return transactionRepo.save(tx);
    }

    // Handle stock OUT (reduce quantity)
    public Transaction stockOut(String productId, int qty, String ref, String remarks, String userId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (product.getQuantity() < qty) {
            throw new RuntimeException("Not enough stock available");
        }

        product.setQuantity(product.getQuantity() - qty);
        productRepo.save(product);

        Transaction tx = Transaction.builder()
                .productId(productId)
                .type(TransactionType.OUT)
                .quantity(qty)
                .referenceNumber(ref)
                .remarks(remarks)
                .userId(userId)
                .date(LocalDateTime.now())
                .build();

        return transactionRepo.save(tx);
    }
}
