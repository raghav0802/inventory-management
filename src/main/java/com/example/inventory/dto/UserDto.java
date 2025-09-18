package com.example.inventory.dto;



import lombok.Data;
import java.time.LocalDate;

@Data
public class UserDto {
    private String id;
    private String username;
    private String role;
    private String fullName;
    private LocalDate createdAt;
}
