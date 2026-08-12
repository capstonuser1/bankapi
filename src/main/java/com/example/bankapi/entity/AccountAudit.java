package com.example.bankapi.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.hibernate.annotations.ColumnDefault;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "ACCOUNT_AUDIT")
public class AccountAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AUDIT_ID", nullable = false)
    private Long id;

    @NotNull
    @Column(name = "ACCOUNT_ID", nullable = false)
    private Long accountId;

    @Column(name = "OLD_BALANCE", precision = 15, scale = 2)
    private BigDecimal oldBalance;

    @Column(name = "NEW_BALANCE", precision = 15, scale = 2)
    private BigDecimal newBalance;

    @NotNull
    @ColumnDefault("SYSTIMESTAMP")
    @Column(name = "CHANGED_AT", nullable = false)
    private Instant changedAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAccountId() {
        return accountId;
    }

    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }

    public BigDecimal getOldBalance() {
        return oldBalance;
    }

    public void setOldBalance(BigDecimal oldBalance) {
        this.oldBalance = oldBalance;
    }

    public BigDecimal getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(BigDecimal newBalance) {
        this.newBalance = newBalance;
    }

    public Instant getChangedAt() {
        return changedAt;
    }

    public void setChangedAt(Instant changedAt) {
        this.changedAt = changedAt;
    }

}