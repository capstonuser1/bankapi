package com.example.bankapi.service;

import com.example.bankapi.dto.AccountDto;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Customer;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.CustomerRepository;
import com.example.bankapi.repository.TransactionRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class AccountServiceIntegrationTest {

    @Autowired
    private AccountService accountService;

    @MockBean
    private AccountRepository accountRepository;

    @MockBean
    private TransactionRepository transactionRepository;

    @MockBean
    private CustomerRepository customerRepository;

    @Test
    void getAllAccounts_returnsMappedDtos() {
        Customer cust = new Customer(1L, "CUST-1", "Alice", "a@example.com", LocalDate.now());
        Account a1 = new Account(10L, "ACC-1", "SAVINGS", new BigDecimal("100.00"), LocalDate.now());
        a1.setCustomer(cust);

        when(accountRepository.getAccounts()).thenReturn(List.of(a1));

        List<AccountDto> dtos = accountService.getAllAccounts();

        assertEquals(1, dtos.size());
        assertEquals(a1.getAccountNumber(), dtos.get(0).getAccountNumber());
        assertEquals(a1.getBalance(), dtos.get(0).getBalance());
    }
}
