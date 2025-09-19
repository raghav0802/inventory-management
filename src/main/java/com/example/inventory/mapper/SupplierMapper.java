package com.example.inventory.mapper;

import com.example.inventory.dto.SupplierDto;
import com.example.inventory.model.Supplier;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class SupplierMapper {

    public static SupplierDto toDto(Supplier supplier) {
        if (supplier == null) return null;

        SupplierDto dto = new SupplierDto();
        dto.setId(supplier.getId());
        dto.setCompanyName(supplier.getCompanyName());
        dto.setContactPerson(supplier.getContactPerson());
        dto.setEmail(supplier.getEmail());
        dto.setPhone(supplier.getPhone());
        dto.setAddress(supplier.getAddress());
        if (supplier.getCreatedAt() != null) {
            dto.setCreatedAt(LocalDateTime.now());
        }


        return dto;
    }

    public static Supplier toEntity(SupplierDto dto) {
        if (dto == null) return null;

        return Supplier.builder()
                .id(dto.getId())
                .companyName(dto.getCompanyName())
                .contactPerson(dto.getContactPerson())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .address(dto.getAddress())
                .createdAt(dto.getCreatedAt())
                .build();
    }
}
