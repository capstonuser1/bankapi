package com.example.bankapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;

@Entity
@Table(name = "TRANSACTIONS")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TXN_ID", nullable = false)
    private Long txnId;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "ACCOUNT_ID", nullable = false)
    private Account account;

    @Size(max = 12)
    @NotNull
    @Column(name = "TXN_TYPE", nullable = false, length = 12)
    private String txnType;

    @NotNull
    @Column(name = "AMOUNT", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Size(max = 10)
    @NotNull
    @ColumnDefault("'COMPLETED'")
    @Column(name = "STATUS", nullable = false, length = 10)
    private String status;


    @ColumnDefault("SYSTIMESTAMP")
    @Column(name = "TXN_DATE")
    private Instant txnDate;

    @Size(max = 255)
    @Column(name = "DESCRIPTION")
    private String description;

    public Transaction() {

    }

    public Transaction(Long txnId, Account account, String txnType, BigDecimal amount, String status, Instant txnDate, String description) {
        this.txnId = txnId;
        this.account = account;
        this.txnType = txnType;
        this.amount = amount;
        this.status = status;
        this.txnDate = txnDate;
        this.description = description;
    }

    public Long getTxnId() {
        return txnId;
    }

    public void setTxnId(Long txnId) {
        this.txnId = txnId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getTxnType() {
        return txnType;
    }

    public void setTxnType(String txnType) {
        this.txnType = txnType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Instant getTxnDate() {
        return txnDate;
    }

    public void setTxnDate(Instant txnDate) {
        this.txnDate = txnDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}