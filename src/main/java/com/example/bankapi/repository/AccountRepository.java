package com.example.bankapi.repository;

import com.example.bankapi.entity.Account;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    @Query(value = "SELECT * FROM Accounts c",  nativeQuery = true )
    List<Account> getAccounts();

    @Query(value = "UPDATE Accounts SET BALANCE = BALANCE + :amount WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    void incrementAccountBalance(@Param("accountId") Number accountId, @Param("amount") Number amount);

    @Query(value = "UPDATE Accounts SET BALANCE = BALANCE - :amount WHERE ACCOUNT_ID = :accountId", nativeQuery = true)
    void decrementAccountBalance(@Param("accountId") Number accountId, @Param("amount") Number amount);
}
