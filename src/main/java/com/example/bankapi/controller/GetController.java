package com.example.bankapi.controller;

import com.example.bankapi.dto.AccountDto;
import com.example.bankapi.dto.CustomerDto;
import com.example.bankapi.dto.TransactionDto;
import com.example.bankapi.model.Account;
import com.example.bankapi.service.AccountService;
import com.example.bankapi.service.TransferService;
import com.example.bankapi.service.DownstreamAccountService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.io.Console;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
public class GetController {

    private final AccountService accountService;
    private final DownstreamAccountService downstreamAccountService;

    public GetController(AccountService accountService, DownstreamAccountService downstreamAccountService, TransferService transferService) {
        this.accountService = accountService;
        this.downstreamAccountService = downstreamAccountService;
    }

    @GetMapping
    public ResponseEntity<List<AccountDto>> getAll() {
        List<AccountDto> accounts = accountService.getAllAccounts();
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/customeraccounts")
    public ResponseEntity<List<AccountDto>> getAllByCustomerNumber(@RequestParam("customerNumber") String customerNumber) {
        List<AccountDto> accounts = accountService.getAllAccountsByCustomerNumber(customerNumber);
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/subjectaccounts")
    public ResponseEntity<List<AccountDto>> getAllBySubject(@AuthenticationPrincipal Jwt jwt) {
        String subject = jwt.getSubject();
        List<AccountDto> accounts = accountService.getAllAccountsBySubject(subject);
        if (accounts.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<List<TransactionDto>> getById(@PathVariable Long id) {
        List<TransactionDto> transactions = accountService.getTransactions(id);
        if (transactions.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(transactions);
    }

    @GetMapping("customers")
    public ResponseEntity<List<CustomerDto>> getCustomers() {
        List<CustomerDto> customers = accountService.getCustomers();
        if (customers.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(List.of());
        }
        return ResponseEntity.ok(customers);
    }

    // TODO 6: Complete this endpoint.
// @AuthenticationPrincipal instructs Spring Security to inject the validated Jwt
// from the SecurityContext directly as a method parameter.
// This is cleaner than calling SecurityContextHolder.getContext() manually.
    @GetMapping("/me")
    public Map<String, Object> getCurrentUser(@AuthenticationPrincipal Jwt jwt) {
        // TODO 7: Return a Map containing:
        //   "subject"            -- jwt.getSubject()
        //   "issuer"             -- jwt.getIssuer().toString()
        //   "scopes"             -- jwt.getClaimAsString("scope")
        //   "tokenExpiry"        -- jwt.getExpiresAt().toString()
        //   "roles"              -- jwt.getClaimAsStringList("roles"), or an empty list if null
        //   "preferredUsername"  -- jwt.getClaimAsString("preferred_username"), or "not present"
        //   "fullName"           -- jwt.getClaimAsString("name"), or "not present"
        //
        // The service token will NOT have "roles", "preferred_username", or "name"
        // because the token customizer in the Authorization Server only adds those
        // for user-context tokens. This difference is the main thing to observe.
        assert jwt.getExpiresAt() != null;

        Console console = System.console();
        console.printf("Current user: %s%n", jwt);

        return Map.of(
                "Subject", jwt.getSubject(),
                "Issuer", jwt.getIssuer().toString(),
                "Scopes", jwt.getClaimAsString("scope"),
                "TokenExpiry", jwt.getExpiresAt().toString(),
                "Roles", jwt.getClaimAsStringList("roles") != null ? jwt.getClaimAsStringList("roles") : List.of(),
                "PreferredUsername", jwt.getClaimAsString("preferred_username") != null ? jwt.getClaimAsString("preferred_username") : "not present",
                "FullName", jwt.getClaimAsString("name") != null ? jwt.getClaimAsString("name") : "not present"
        ); // Replace with your implementation
    }

    // TODO 24: Add this endpoint to AccountController.
    // It is protected and requires an authenticated caller.
    // The inbound request uses the caller's token.
    // The outbound call to the downstream service uses the service's own token.
    @GetMapping("/downstream")
    public List<Account> getFromDownstream() {
        // TODO: call downstreamAccountService.fetchAllFromDownstream() and return the result
        return downstreamAccountService.fetchAllFromDownstream();

    }
}