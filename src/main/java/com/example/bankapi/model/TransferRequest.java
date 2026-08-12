package com.example.bankapi.model;


import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        String transactionType,
        String fromAccountNumber,
        String toAccountNumber,
        @Positive BigDecimal amount,
        String description
        ) {
}
