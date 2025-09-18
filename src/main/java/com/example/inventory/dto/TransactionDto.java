package com.example.inventory.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class TransactionDto {
    private String id;
    private String productId;
    private String type; // IN / OUT
    private int quantity;
    private String referenceNumber;
    private String remarks;
    private String userId;
    private LocalDateTime date;


}
