package com.example.bankapi.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record TransferRequest(
        @NotBlank String transactionType,
        @NotBlank Number fromAccountId,
        @NotBlank Number toAccountId,
        @Positive BigDecimal amount,
        @NotBlank String description
        ) {
}
