package com.example.bankapi.model;


public record UpdateAccountStatusRequest(
        String accountNumber,
        String status
) {
}