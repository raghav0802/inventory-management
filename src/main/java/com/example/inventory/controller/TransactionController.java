package com.example.inventory.controller;

import com.example.inventory.model.Transaction;
import com.example.inventory.service.TransactionService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService service;

    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping
    public List<Transaction> getAllTransactions() {
        return service.getAll();
    }

    @GetMapping("/product/{productId}")
    public List<Transaction> getByProduct(@PathVariable String productId) {
        return service.getByProductId(productId);
    }

    @PostMapping("/stock-in")
    public Transaction stockIn(
            @RequestParam String productId,
            @RequestParam int qty,
            @RequestParam(required = false) String ref,
            @RequestParam(required = false) String remarks,
            @RequestParam(defaultValue = "system") String userId
    ) {
        return service.stockIn(productId, qty, ref, remarks, userId);
    }

    @PostMapping("/stock-out")
    public Transaction stockOut(
            @RequestParam String productId,
            @RequestParam int qty,
            @RequestParam(required = false) String ref,
            @RequestParam(required = false) String remarks,
            @RequestParam(defaultValue = "system") String userId
    ) {
        return service.stockOut(productId, qty, ref, remarks, userId);
    }
}
