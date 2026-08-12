package com.example.bankapi.entity;

import com.example.bankapi.entity.Transaction;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Instant;

@Entity
@Table(name = "TRANSFERS")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "TRANSFER_ID", nullable = false)
    private Long transferId;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "DEBIT_TXN_ID", nullable = false)
    private Transaction debitTxn;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @OnDelete(action = OnDeleteAction.RESTRICT)
    @JoinColumn(name = "CREDIT_TXN_ID", nullable = false)
    private Transaction creditTxn;

    @NotNull
    @ColumnDefault("SYSTIMESTAMP")
    @Column(name = "CREATED_DATE", nullable = false)
    private Instant createdDate;

    public Long getTransferId() {
        return transferId;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public Transaction getDebitTxn() {
        return debitTxn;
    }

    public void setDebitTxn(Transaction debitTxn) {
        this.debitTxn = debitTxn;
    }

    public Transaction getCreditTxn() {
        return creditTxn;
    }

    public void setCreditTxn(Transaction creditTxn) {
        this.creditTxn = creditTxn;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

}