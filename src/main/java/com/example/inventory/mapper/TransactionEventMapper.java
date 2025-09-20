package com.example.inventory.mapper;

import com.example.inventory.model.Transaction;
import com.example.inventory.kafka.TransactionEvent;

import java.time.LocalDateTime;

public class TransactionEventMapper {

    public static TransactionEvent toEvent(Transaction t, String typeOverride) {
        return new TransactionEvent(
                t.getId(),
                t.getProductId(),
                t.getUserId(),
                t.getQuantity(),
                typeOverride != null ? typeOverride : t.getType().name(), // IN / OUT / CANCELED
                LocalDateTime.now()
        );
    }
}
