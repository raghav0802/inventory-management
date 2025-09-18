package com.example.inventory.service;

import com.example.inventory.dto.TransactionDto;
import com.example.inventory.exception.InsufficientStockException;
import com.example.inventory.exception.ProductNotFoundException;
import com.example.inventory.mapper.TransactionMapper;
import com.example.inventory.model.*;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final ProductRepository productRepo;

    public TransactionService(TransactionRepository transactionRepo, ProductRepository productRepo) {
        this.transactionRepo = transactionRepo;
        this.productRepo = productRepo;
    }

    public List<TransactionDto> getAll() {
        return transactionRepo.findAll()
                .stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getByProductId(String productId) {
        return transactionRepo.findByProductId(productId)
                .stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    public List<TransactionDto> getTransactionsByUserId(String userId) {
        return transactionRepo.findByUserId(userId)
                .stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }


    // Handle stock IN (add quantity)
    public TransactionDto stockIn(String productId, int qty, String ref, String remarks, String userId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

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

        return TransactionMapper.toDto(transactionRepo.save(tx));
    }

    // Handle stock OUT (reduce quantity)
    public TransactionDto stockOut(String productId, int qty, String ref, String remarks, String userId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getQuantity() < qty) {
            throw new InsufficientStockException(productId, qty, product.getQuantity());
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

        return TransactionMapper.toDto(transactionRepo.save(tx));
    }
    public List<TransactionDto> getTransactionsByUserAndDateRange(String userId, LocalDateTime from, LocalDateTime to) {
        return transactionRepo.findByUserIdAndDateBetween(userId, from, to)
                .stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }
}
