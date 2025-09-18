package com.example.inventory.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.*;

import java.time.LocalDate;

@Document(collection = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Supplier {
    @Id
    private String id;

    private String companyName;
    private String contactPerson;
    private String email;
    private String phone;
    private String address;

    private LocalDate createdAt;
}
