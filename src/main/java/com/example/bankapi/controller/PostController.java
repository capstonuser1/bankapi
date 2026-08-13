package com.example.bankapi.controller;

import com.example.bankapi.model.TransactionStatus;
import com.example.bankapi.model.TransferRequest;
import com.example.bankapi.model.TransferResponse;
import com.example.bankapi.model.UpdateAccountStatusRequest;
import com.example.bankapi.service.AccountService;
import com.example.bankapi.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transfers")
public class PostController {

    private final TransferService transferService;
    private final AccountService accountService;

    public PostController(TransferService transferService, AccountService accountService) {
        this.transferService = transferService;
        this.accountService = accountService;
    }

    @PostMapping("/dotransfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse status = transferService.doTransfer(request);
        if(status.status() == TransactionStatus.FAILED){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(status);
    }

    @PostMapping("/dotransaction")
    public ResponseEntity<TransferResponse> doTransaction(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.doTransaction(request);
        if(response.status() == TransactionStatus.FAILED){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    @PostMapping("/doaccountupdatestatus")
    public String doAccountUpdateStatus(@Valid @RequestBody UpdateAccountStatusRequest request) {
        String response = accountService.updateAccountStatus(request.accountNumber(), request.status());
        return response;
    }

}