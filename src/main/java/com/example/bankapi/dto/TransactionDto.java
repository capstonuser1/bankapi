package com.example.bankapi.dto;

import java.io.Serializable;
import java.math.BigDecimal;

public class TransactionDto implements Serializable {
    private String transactionId;
    private String accountNumber;
    private String transactionType;
    private BigDecimal amount;
    private String description;

    public TransactionDto() {
    }

    public TransactionDto(String transactionId, String accountNumber, String transactionType, BigDecimal amount, String description) {
        this.transactionId = transactionId;
        this.accountNumber = accountNumber;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }
}
