package com.example.inventory.exception;

public class InsufficientStockException extends RuntimeException {
    public InsufficientStockException(String productId, int requested, int available) {
        super("Not enough stock for product " + productId +
                ". Requested: " + requested + ", Available: " + available);
    }
}