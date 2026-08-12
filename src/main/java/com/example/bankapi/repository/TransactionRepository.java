package com.example.bankapi.repository;

import com.example.bankapi.entity.Transaction;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query(value = "SELECT * FROM Transactions t WHERE t.account_id = :accountId", nativeQuery = true)
    List<Transaction> getTransactions(@Param("accountId") Long accountId);

    @Query(value = "INSERT INTO Transactions (ACCOUNT_ID, TXN_TYPE, AMOUNT, DESCRIPTION) VALUES (:accountId, :txnType, :amount, :description) RETURNING transaction_id", nativeQuery = true)
    String CreateTransfer(@Param("accountId") Number accountId, @Param("txnType") String txnType, @Param("amount") Number amount, @Param("description") String description);

}
