package com.example.bankapi.repository;

import com.example.bankapi.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT * FROM Accounts c",  nativeQuery = true )
    List<Account> getAccounts();

    @Modifying
    @Query(value = "UPDATE Accounts SET BALANCE = BALANCE + :amount WHERE ACCOUNT_NUMBER = :accountNumber", nativeQuery = true)
    void incrementAccountBalance(@Param("accountNumber") String accountNumber, @Param("amount") Number amount);

    @Modifying
    @Query(value = "UPDATE Accounts SET BALANCE = BALANCE - :amount WHERE ACCOUNT_NUMBER = :accountNumber", nativeQuery = true)
    void decrementAccountBalance(@Param("accountNumber") String accountNumber, @Param("amount") Number amount);

    @Query(value = "SELECT * FROM Accounts c WHERE c.ACCOUNT_NUMBER = :accountNumber", nativeQuery = true)
    Account findAccountByAccountNumber(String accountNumber);
}
