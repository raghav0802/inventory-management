package com.example.inventory.service;

import com.example.inventory.dto.TransactionDto;
import com.example.inventory.exception.InsufficientStockException;
import com.example.inventory.exception.ProductNotFoundException;
import com.example.inventory.exception.TransactionNotFoundException;
import com.example.inventory.kafka.TransactionEvent;
import com.example.inventory.mapper.TransactionEventMapper;
import com.example.inventory.mapper.TransactionMapper;
import com.example.inventory.model.*;
import com.example.inventory.repository.ProductRepository;
import com.example.inventory.repository.TransactionRepository;
import org.springframework.stereotype.Service;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TransactionService {
    private final TransactionRepository transactionRepo;
    private final ProductRepository productRepo;
    private final KafkaTemplate<String, TransactionEvent> kafkaTemplate;

    private static final String TOPIC = "transactions";

    public TransactionService(
            TransactionRepository transactionRepo,
            ProductRepository productRepo,
            KafkaTemplate<String, TransactionEvent> kafkaTemplate
    ) {
        this.transactionRepo = transactionRepo;
        this.productRepo = productRepo;
        this.kafkaTemplate = kafkaTemplate;
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
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);

        Transaction tx = Transaction.builder()
                .productId(productId)
                .type(TransactionType.IN)
                .quantity(qty)
                .referenceNumber(ref)
                .remarks(remarks)
                .userId(userId)
                .date(LocalDateTime.now())
                .status(TransactionStatus.ACTIVE)
                .build();

        Transaction savedTx = transactionRepo.save(tx);

        //  Publish Kafka event
        TransactionEvent event = TransactionEventMapper.toEvent(tx, null);
        kafkaTemplate.send("transaction-events", event);

        return TransactionMapper.toDto(tx);


    }

    // Handle stock OUT (reduce quantity)
    public TransactionDto stockOut(String productId, int qty, String ref, String remarks, String userId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException(productId));

        if (product.getQuantity() < qty) {
            throw new InsufficientStockException(productId, qty, product.getQuantity());
        }

        product.setQuantity(product.getQuantity() - qty);
        product.setUpdatedAt(LocalDateTime.now());
        productRepo.save(product);

        Transaction tx = Transaction.builder()
                .productId(productId)
                .type(TransactionType.OUT)
                .quantity(qty)
                .referenceNumber(ref)
                .remarks(remarks)
                .userId(userId)
                .date(LocalDateTime.now())
                .status(TransactionStatus.ACTIVE)
                .build();

        Transaction savedTx = transactionRepo.save(tx);

        TransactionEvent event = TransactionEventMapper.toEvent(tx, null);
        kafkaTemplate.send("transaction-events", event);

        return TransactionMapper.toDto(tx);
    }
    public List<TransactionDto> getTransactionsByUserAndDateRange(String userId, LocalDateTime from, LocalDateTime to) {
        return transactionRepo.findByUserIdAndDateBetween(userId, from, to)
                .stream()
                .map(TransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    public boolean cancelTransaction(String id) {
        Transaction t = transactionRepo.findById(id)
                .orElseThrow(() -> new TransactionNotFoundException(id));

        Product product = productRepo.findById(t.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(t.getProductId()));

        // Check if already canceled
        if (t.getStatus() == TransactionStatus.CANCELED) {
            return false; // Already canceled
        }

        // Check if transaction is within 24 hours
        LocalDateTime now = LocalDateTime.now();
        Duration diff = Duration.between(t.getDate(), now);

        if (diff.toHours() < 24) {
            // Reverse stock change depending on transaction type
            if (t.getType() == TransactionType.IN) {
                product.setQuantity(product.getQuantity() - t.getQuantity());
            } else if (t.getType() == TransactionType.OUT) {
                product.setQuantity(product.getQuantity() + t.getQuantity());
            }

            productRepo.save(product);

            //Mark transaction as canceled
            t.setStatus(TransactionStatus.CANCELED);
            transactionRepo.save(t);



            // send a specific Kafka cancellation event

            TransactionEvent event = TransactionEventMapper.toEvent(t, "CANCELED");
            kafkaTemplate.send("transaction-events", event);

            return true;
        }
        return false;
    }


}
