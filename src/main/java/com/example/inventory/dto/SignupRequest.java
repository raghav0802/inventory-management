package com.example.inventory.dto;

import lombok.Data;

@Data
public class SignupRequest {
    private String username;
    private String password;
    private String fullName;
    private String role; // optional, default "USER"  // optional
}