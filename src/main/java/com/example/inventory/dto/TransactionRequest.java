package com.example.inventory.dto;

import lombok.Data;

@Data
public class TransactionRequest {
    private String productId;
    private int quantity;
    private String referenceNumber;
    private String remarks;
    private String userId;
}

