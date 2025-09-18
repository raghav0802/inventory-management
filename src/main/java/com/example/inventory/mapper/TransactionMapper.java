package com.example.inventory.mapper;

import com.example.inventory.dto.TransactionDto;
import com.example.inventory.model.Transaction;
import com.example.inventory.model.TransactionType;

public class TransactionMapper {

    public static TransactionDto toDto(Transaction transaction) {
        if (transaction == null) return null;

        TransactionDto dto = new TransactionDto();
        dto.setId(transaction.getId());
        dto.setProductId(transaction.getProductId());
        dto.setType(String.valueOf(transaction.getType()));
        dto.setQuantity(transaction.getQuantity());
        dto.setReferenceNumber(transaction.getReferenceNumber());
        dto.setRemarks(transaction.getRemarks());
        dto.setUserId(transaction.getUserId());
        dto.setDate(transaction.getDate());
        return dto;
    }

    public static Transaction toEntity(TransactionDto dto) {
        if (dto == null) return null;

        return Transaction.builder()
                .id(dto.getId())
                .productId(dto.getProductId())
                .type(TransactionType.valueOf(dto.getType()))
                .quantity(dto.getQuantity())
                .referenceNumber(dto.getReferenceNumber())
                .remarks(dto.getRemarks())
                .userId(dto.getUserId())
                .date(dto.getDate())
                .build();
    }
}
