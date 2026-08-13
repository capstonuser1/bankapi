package com.example.bankapi.service;

import com.example.bankapi.dto.AccountDto;
import com.example.bankapi.dto.CustomerDto;
import com.example.bankapi.dto.TransactionDto;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Customer;
import com.example.bankapi.entity.Transaction;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.TransactionRepository;
import com.example.bankapi.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private AccountService accountService;

    @Test
    void getAllAccounts_mapsCorrectly() {
        Customer cust = new Customer(1L, "CUST-1", "Alice", "a@example.com", LocalDate.now());
        Account a1 = new Account(10L, "ACC-1", "SAVINGS", new BigDecimal("100.00"), LocalDate.now());
        a1.setCustomer(cust);
        Account a2 = new Account(11L, "ACC-2", "CHECKING", new BigDecimal("250.50"), LocalDate.now());
        a2.setCustomer(cust);

        when(accountRepository.getAccounts()).thenReturn(List.of(a1, a2));

        List<AccountDto> dtos = accountService.getAllAccounts();

        assertEquals(2, dtos.size());
        assertEquals(a1.getAccountNumber(), dtos.get(0).getAccountNumber());
        assertEquals(a1.getBalance(), dtos.get(0).getBalance());
        assertEquals(a2.getAccountNumber(), dtos.get(1).getAccountNumber());
    }

    @Test
    void getAllAccountsBySubject_filtersCorrectly() {
        Customer alice = new Customer(1L, "CUST-A", "Alice", "a@x.com", LocalDate.now());
        Customer bob = new Customer(2L, "CUST-B", "Bob", "b@x.com", LocalDate.now());

        Account a1 = new Account(100L, "A1", "SAV", new BigDecimal("10.00"), LocalDate.now());
        a1.setCustomer(alice);
        Account a2 = new Account(101L, "A2", "CHK", new BigDecimal("20.00"), LocalDate.now());
        a2.setCustomer(bob);

        when(accountRepository.getAccounts()).thenReturn(List.of(a1, a2));

        List<AccountDto> result = accountService.getAllAccountsBySubject("CUST-A");

        assertEquals(1, result.size());
        assertEquals(a1.getAccountNumber(), result.get(0).getAccountNumber());
    }

    @Test
    void getTransactions_filtersByAccountId() {
        Customer cust = new Customer(5L, "CUST-X", "X", "x@x.com", LocalDate.now());
        Account accMatch = new Account(200L, "ACC-X", "SAV", new BigDecimal("0.00"), LocalDate.now());
        accMatch.setCustomer(cust);
        Account accOther = new Account(201L, "ACC-Y", "CHK", new BigDecimal("0.00"), LocalDate.now());
        accOther.setCustomer(cust);

        Transaction t1 = new Transaction(1L, accMatch, "CREDIT", new BigDecimal("5.00"), "COMPLETED", Instant.now(), "desc1");
        Transaction t2 = new Transaction(2L, accOther, "DEBIT", new BigDecimal("2.00"), "COMPLETED", Instant.now(), "desc2");

        when(transactionRepository.getTransactions()).thenReturn(List.of(t1, t2));

        List<TransactionDto> dtos = accountService.getTransactions("ACC-X");

        assertEquals(1, dtos.size());
        assertEquals(1L, dtos.get(0).getTransactionId());
        assertEquals(accMatch.getAccountNumber(), dtos.get(0).getAccountNumber());
    }

    @Test
    void getCustomers_mapsCorrectly() {
        Customer c1 = new Customer(7L, "C7", "C Seven", "c7@x.com", LocalDate.now());
        Customer c2 = new Customer(8L, "C8", "C Eight", "c8@x.com", LocalDate.now());

        when(customerRepository.getCustomers()).thenReturn(List.of(c1, c2));

        List<CustomerDto> dtos = accountService.getCustomers();

        assertEquals(2, dtos.size());
        assertEquals(c1.getCustomerNumber(), dtos.get(0).getCustomerNumber());
        assertEquals(c2.getEmail(), dtos.get(1).getEmail());
    }
}
