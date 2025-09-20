package com.example.inventory.kafka;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionEvent {
    private String transactionId;
    private String productId;
    private String userId;
    private int quantity;
    private String type; // IN / OUT
    private LocalDateTime date;
}
