package com.example.bankapi.service;

import com.example.bankapi.dto.TransactionStatsDto;
import com.example.bankapi.entity.Account;
import com.example.bankapi.entity.Transaction;
import com.example.bankapi.model.TransactionStatus;
import com.example.bankapi.model.TransferRequest;
import com.example.bankapi.model.TransferResponse;
import com.example.bankapi.repository.AccountRepository;
import com.example.bankapi.repository.TransactionRepository;
import com.example.bankapi.repository.TransferRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;


@Service
public class TransferService {

    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    private final TransferRepository transferRepository;
    private final MessagePublisher messagePublisher;

    public TransferService(TransactionRepository transactionRepository, AccountRepository accountRepository, TransferRepository transferRepository, MessagePublisher messagePublisher) {
        this.transactionRepository = transactionRepository;
        this.accountRepository = accountRepository;
        this.transferRepository = transferRepository;
        this.messagePublisher = messagePublisher;
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

            TransactionStatsDto dt =new TransactionStatsDto("TRANSFER_IN", request.amount().setScale(2, BigDecimal.ROUND_HALF_UP));
            messagePublisher.publish(dt);

            TransactionStatsDto dt2 =new TransactionStatsDto("TRANSFER_OUT", request.amount().setScale(2, BigDecimal.ROUND_HALF_UP));
            messagePublisher.publish(dt2);
            return new TransferResponse(creditTransactionId.toString(), TransactionStatus.COMPLETE, "Transfer Completed Successfully.");
        }
        catch (Exception e) {
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (org.springframework.transaction.NoTransactionException ignored) {
                // No active transaction to rollback
            }
            return new TransferResponse(null, TransactionStatus.FAILED, "Transfer Failed.");
        }
    }

    @Transactional
    public TransferResponse doTransaction(TransferRequest request) {
        try {
            if(Objects.equals(request.transactionType(), "DEPOSIT") || Objects.equals(request.transactionType(), "CREDIT")) {
                accountRepository.incrementAccountBalance(request.toAccountNumber(), request.amount());
            } else {
                accountRepository.decrementAccountBalance(request.toAccountNumber(), request.amount());
            }

            Transaction creditTxn = new Transaction();
            creditTxn.setAccount(request.toAccountNumber() != null ? accountRepository.findAccountByAccountNumber(request.toAccountNumber()) : null);
            creditTxn.setTxnType(request.transactionType());
            creditTxn.setAmount((BigDecimal) request.amount());
            creditTxn.setTxnDate(Instant.now());
            creditTxn.setStatus("COMPLETED");
            creditTxn.setDescription(request.description());
            Transaction creditSaved = transactionRepository.saveAndFlush(creditTxn);
            TransactionStatsDto dt2 =new TransactionStatsDto(request.transactionType(), request.amount().setScale(2, BigDecimal.ROUND_HALF_UP));
            messagePublisher.publish(dt2);
            Long creditTransactionId = creditSaved.getTxnId();
            return new TransferResponse(creditTransactionId.toString(), TransactionStatus.COMPLETE, "Transaction Completed Successfully.");
        }
        catch (Exception e) {
            try {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } catch (org.springframework.transaction.NoTransactionException ignored) {
                // No active transaction to rollback
            }
            return new TransferResponse(null, TransactionStatus.FAILED, "Transaction Failed.");
        }
    }
}