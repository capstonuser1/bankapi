package com.example.bankapi.repository;

import com.example.bankapi.entity.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface TransferRepository extends JpaRepository<Transfer, Long> {

    @Modifying
    @Query(value="insert into Transfers (DEBIT_TXN_ID, CREDIT_TXN_ID) values (:debitTxnId, :creditTxnId)", nativeQuery = true)
    void createTransfer(Long debitTxnId, Long creditTxnId);

}
