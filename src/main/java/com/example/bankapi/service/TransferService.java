package com.example.bankapi.service;

import com.example.bankapi.dto.TransactionStatsDto;
import com.example.bankapi.model.Account;
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
            //Reduce balance in from Account
            accountRepository.decrementAccountBalance(request.fromAccountId(), request.amount());

            //Increase Balance in To Account
            accountRepository.incrementAccountBalance(request.toAccountId(), request.amount());

            String creditTransactionId = transactionRepository.CreateTransfer(request.toAccountId(), "CREDIT", request.amount(), request.description());
            String debitTransactionId = transactionRepository.CreateTransfer(request.fromAccountId(), "DEBIT", request.amount(), request.description());

            transferRepository.createTransfer(debitTransactionId, creditTransactionId);
            // kafka code
            TransactionStatsDto dtIn =new TransactionStatsDto("TRANSFER_IN", request.amount());
            messagePublisher.publish_test(dtIn);
            TransactionStatsDto dtOut =new TransactionStatsDto("TRANSFER_OUT", request.amount());
            messagePublisher.publish_test(dtOut);
            return new TransferResponse(null, TransactionStatus.COMPLETE);
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new TransferResponse(null, TransactionStatus.FAILED);
        }
    }

    @Transactional
    public TransferResponse doTransaction(TransferRequest request) {
        try {
            String transactionId;
            if(Objects.equals(request.transactionType(), "CREDIT")) {
                //Increase Balance in To Account
                accountRepository.incrementAccountBalance(request.toAccountId(), request.amount());
                transactionId = transactionRepository.CreateTransfer(request.toAccountId(), "CREDIT", request.amount(), request.description());

            } else {
                //Reduce balance in from Account
                accountRepository.decrementAccountBalance(request.toAccountId(), request.amount());
                transactionId = transactionRepository.CreateTransfer(request.toAccountId(), "DEBIT", request.amount(), request.description());

            }
            return new TransferResponse(transactionId, TransactionStatus.COMPLETE);
        }
        catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return new TransferResponse(null, TransactionStatus.FAILED);
        }
    }
}