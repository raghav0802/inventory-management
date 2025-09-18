package com.example.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;
import java.time.LocalDateTime;

@Document(collection = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    private String id;

    private String productId;   // Reference to Product
    private TransactionType type; // IN or OUT
    private Integer quantity;
    private String referenceNumber; // e.g., invoice ID, sales order
    private String remarks;
    private String userId; // Who did the transaction

    private LocalDateTime date;
}
