package com.example.bankapi.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

public class AccountDto implements Serializable {
    private long accountId;
    private String accountNumber;
    private String accountType;
    private String accountStatus;
    private BigDecimal balance;
    private LocalDate openedDate;

    public AccountDto() {
    }

    public AccountDto(long accountId, String accountNumber, String accountType, String accountStatus, BigDecimal balance, LocalDate openedDate) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.accountStatus = accountStatus;
        this.balance = balance;
        this.openedDate = openedDate;
    }

    public long getAccountId() {
        return accountId;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public BigDecimal getBalance() {
        return balance;
    }

}
