package com.example.bankapi.controller;

import com.example.bankapi.model.TransactionStatus;
import com.example.bankapi.model.TransferRequest;
import com.example.bankapi.model.TransferResponse;
import com.example.bankapi.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transfers")
public class PostController {

    private final TransferService transferService;

    public PostController(TransferService transferService) {
        this.transferService = transferService;
    }

    @PostMapping("/dotransfer")
    public ResponseEntity<TransferResponse> transfer(@Valid @RequestBody TransferRequest request) {
        TransferResponse status = transferService.doTransfer(request);
        if(status.status() == TransactionStatus.FAILED){
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/dotransaction")
    public ResponseEntity<TransferResponse> doTransaction(@Valid @RequestBody TransferRequest request) {
        TransferResponse response = transferService.doTransaction(request);
        if(response.status() == TransactionStatus.FAILED){
            return ResponseEntity.status(500).build();
        }
        return ResponseEntity.ok().build();
    }
}