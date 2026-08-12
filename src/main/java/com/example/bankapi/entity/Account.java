package com.example.bankapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.time.LocalDate;

@Table(name = "accounts")
@Entity
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ACCOUNT_ID", nullable = false)
    private long accountId;

    @Size(max = 12)
    @NotNull
    @Column(name = "ACCOUNT_NUMBER", nullable = false, length = 12)
    private String accountNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "CUSTOMER_ID", nullable = false)
    private Customer customer;

    @Size(max = 20)
    @NotNull
    @Column(name = "ACCOUNT_TYPE", nullable = false, length = 20)
    private String accountType;

    @Size(max = 8)
    @NotNull
    @ColumnDefault("'INACTIVE'")
    @Column(name = "ACCOUNT_STATUS", nullable = false, length = 8)
    private String accountStatus;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "BALANCE", nullable = false, precision = 15, scale = 2)
    private BigDecimal balance;

    @NotNull
    @ColumnDefault("SYSDATE")
    @Column(name = "OPENED_DATE", nullable = false)
    private LocalDate openedDate;

    public Account() {
    }

    public Account(long accountId, String accountNumber, String accountType, BigDecimal balance, LocalDate openedDate) {
        this.accountId = accountId;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.balance = balance;
        this.openedDate = openedDate;
    }

    public void setId(Long accountId) {
        this.accountId = accountId;
    }

    public LocalDate getOpenedDate() {
        return openedDate;
    }

    public void setOpenedDate(LocalDate openedDate) {
        this.openedDate = openedDate;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public Long getAccountId() {
        return accountId;
    }

}
