package com.example.bankapi.service;

import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Transaction;
import com.example.bankapi.model.TransactionStatus;
import com.example.bankapi.model.TransferRequest;
import com.example.bankapi.model.TransferResponse;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.TransactionRepository;
import com.example.bankapi.repository.TransferRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.RequestBody;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;


@Service
public class TransferService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;

    public TransferService(TransactionRepository transactionRepository, AccountRepository accountRepository, TransferRepository transferRepository) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
    }

    @Transactional
    public TransferResponse doTransfer(TransferRequest request) {
        try {
            accountRepository.decrementAccountBalance(request.fromAccountNumber(), request.amount());
            accountRepository.incrementAccountBalance(request.toAccountNumber(), request.amount());

            Account toAccount = accountRepository.findAccountByAccountNumber(request.toAccountNumber());
            Account fromAccount = accountRepository.findAccountByAccountNumber(request.fromAccountNumber());

            Transaction creditTxn = new Transaction();
            creditTxn.setAccount(toAccount);
            creditTxn.setTxnType("TRANSFER_IN");
            creditTxn.setAmount((BigDecimal) request.amount());
            creditTxn.setStatus("COMPLETED");
            //Timestamp timestamp =  new Timestamp(System.currentTimeMillis());
            //Instant instant = Instant.now();
            //creditTxn.setTxnDate(Timestamp.from(Instant.now()));
            creditTxn.setDescription(request.description());
            Transaction creditSaved = transactionRepository.saveAndFlush(creditTxn);
            Long creditTransactionId = creditSaved.getTxnId();

            Transaction debitTxn = new Transaction();
            debitTxn.setAccount(fromAccount);
            debitTxn.setTxnType("TRANSFER_OUT");
            debitTxn.setAmount((BigDecimal) request.amount());
            debitTxn.setStatus("COMPLETED");
            //Timestamp timestamp =  new Timestamp(System.currentTimeMillis());
            //Instant instant = Instant.now();
            //debitTxn.setTxnDate(Timestamp.from(Instant.now()));
            debitTxn.setDescription(request.description());
            Transaction debitSaved = transactionRepository.saveAndFlush(debitTxn);
            Long debitTransactionId = debitSaved.getTxnId();

            transferRepository.createTransfer(debitTransactionId, creditTransactionId);
            return new TransferResponse(creditTransactionId.toString(), TransactionStatus.COMPLETE, "Transfer Completed Successfully.");
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new TransferResponse(null, TransactionStatus.FAILED, "Transfer Failed.");
        }
    }

    @Transactional
    public TransferResponse doTransaction(TransferRequest request) {
        try {
            if(Objects.equals(request.transactionType(), "CREDIT")) {
                accountRepository.incrementAccountBalance(request.toAccountNumber(), request.amount());
            } else {
                accountRepository.decrementAccountBalance(request.toAccountNumber(), request.amount());
            }
            return new TransferResponse(null, TransactionStatus.COMPLETE, "Transaction Completed Successfully.");
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new TransferResponse(null, TransactionStatus.FAILED, "Transaction Failed.");
        }
    }
}