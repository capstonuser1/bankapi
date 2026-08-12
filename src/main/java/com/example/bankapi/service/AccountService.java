package com.example.bankapi.service;

import com.example.bankapi.dto.AccountDto;
import com.example.bankapi.dto.CustomerDto;
import com.example.bankapi.dto.TransactionDto;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Customer;
import com.example.bankapi.entity.Transaction;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.CustomerRepository;
import com.example.bankapi.repository.TransactionRepository;
import jakarta.transaction.Transactional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final CustomerRepository customerRepository;

    public AccountService(AccountRepository accountRepository,
                          TransactionRepository transactionRepository,
                          CustomerRepository customerRepository) {
        this.accountRepository = accountRepository;
        this.transactionRepository = transactionRepository;
        this.customerRepository = customerRepository;
    }

    @Transactional
    @Cacheable(value = "accounts", keyGenerator = "customGenerator")
    //@PreAuthorize("hasRole('account_holder')")
    public List<AccountDto> getAllAccounts(){
        return accountRepository.getAccounts()
                .stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional
    @Cacheable(value = "accounts", keyGenerator = "customGenerator")
    //@PreAuthorize("hasRole('account_holder')")
    public List<AccountDto> getAllAccountsBySubject(String subject){
        return accountRepository.getAccounts()
                .stream()
                .filter(account -> account.getCustomer().getCustomerNumber().equals(subject))
                .map(this::toDto)
                .toList();
    }

    @Transactional
    public List<TransactionDto> getTransactions(Long accountId) {
        return transactionRepository.getTransactions(accountId)
                .stream()
                .filter(transaction -> transaction.getAccount().getAccountId().equals(accountId))
                .map(this::toTransactionDto)
                .toList();
    }

    @Transactional
    @Cacheable(value = "customers", keyGenerator = "customGenerator")
    public List<CustomerDto> getCustomers() {
        return customerRepository.getCustomers()
                .stream()
                .map(this::toCustomerDto)
                .toList();
    }

    private CustomerDto toCustomerDto(Customer customer) {
        return new CustomerDto(
                customer.getCustomerId(),
                customer.getCustomerNumber(),
                customer.getFullName(),
                customer.getEmail(),
                customer.getCreatedDate()
        );
    }

    private TransactionDto toTransactionDto(Transaction transaction) {
        return new TransactionDto(
                transaction.getTxnId(),
                transaction.getAccount().getAccountNumber(),
                transaction.getTxnType(),
                transaction.getAmount(),
                transaction.getDescription()
        );
    }

    private AccountDto toDto(Account account) {
        return new AccountDto(
                account.getAccountId(),
                account.getAccountNumber(),
                account.getAccountType(),
                account.getAccountStatus(),
                account.getBalance(),
                account.getOpenedDate()
        );
    }


}