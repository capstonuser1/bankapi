package com.example.bankapi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;

public class TransactionDto implements Serializable {
    private Long transactionId;
    private String accountNumber;
    private String transactionType;
    private String transactionStatus;
    private Instant transactionDate;
    private BigDecimal amount;
    private String description;

    public TransactionDto() {
    }

    public TransactionDto(Long transactionId, String accountNumber, String transactionType, String transactionStatus, Instant transactionDate, BigDecimal amount, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.transactionStatus = transactionStatus;
        this.transactionDate = transactionDate;
        this.amount = amount;
        this.description = description;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public Instant getTransactionDate() {
        return transactionDate;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
