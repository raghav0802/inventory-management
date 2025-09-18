package com.example.inventory.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SupplierDto {
    private String id;
    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;
    private LocalDateTime createdAt;

}
