package com.example.inventory.controller;

import com.example.inventory.dto.TransactionDto;
import com.example.inventory.model.Transaction;
import com.example.inventory.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<TransactionDto>> getAllTransactions() {
        return ResponseEntity.ok(service.getAll());
    }



    @GetMapping("/product/{productId}")
    public ResponseEntity<List<TransactionDto>> getByProduct(@PathVariable String productId) {
        return ResponseEntity.ok(service.getByProductId(productId));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionDto>> getByUser(@PathVariable String userId,
                                                          @RequestParam(required = false) String from,
                                                          @RequestParam(required = false) String to) {
        if (from != null && to != null) {
            LocalDateTime fromDate = LocalDateTime.parse(from);
            LocalDateTime toDate = LocalDateTime.parse(to);
            return ResponseEntity.ok(service.getTransactionsByUserAndDateRange(userId, fromDate, toDate));
        }
        return ResponseEntity.ok(service.getTransactionsByUserId(userId));
    }


    @PostMapping("/in")
    public ResponseEntity<TransactionDto> stockIn(
            @RequestParam String productId,
            @RequestParam int qty,
            @RequestParam(required = false) String ref,
            @RequestParam(required = false) String remarks,
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(service.stockIn(productId, qty, ref, remarks, userId));
    }

    @PostMapping("/out")
    public ResponseEntity<TransactionDto> stockOut(
            @RequestParam String productId,
            @RequestParam int qty,
            @RequestParam(required = false) String ref,
            @RequestParam(required = false) String remarks,
            @RequestParam String userId
    ) {
        return ResponseEntity.ok(service.stockOut(productId, qty, ref, remarks, userId));
    }

}
