package com.example.bankapi.controller;

import com.example.bankapi.model.Account;
import com.example.bankapi.service.AuditService;
import com.example.bankapi.service.TransferService;
import com.example.bankapi.service.DownstreamAccountService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    private AuditService auditService;
    private final DownstreamAccountService downstreamAccountService;
    private final TransferService transferService;
    public AccountController(AuditService auditService, DownstreamAccountService downstreamAccountService, TransferService transferService) {
        this.auditService = auditService;
        this.downstreamAccountService = downstreamAccountService;
        this.transferService = transferService;
    }

    @GetMapping
    @Cacheable(value = "accounts", keyGenerator = "customGenerator")
    public List<Account> getAll() {
        return transferService.listAccounts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Account> getById(@PathVariable String id) {
        return transferService.listAccounts().stream()
                .filter(a -> a.id().equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(account);
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

    // TODO 10: Add this endpoint to AccountController.
    // For a regular account holder it returns only accounts whose customerId
    // matches their sub. For a teller or auditor it returns all accounts.
    //
    // Read jwt.getSubject() and jwt.getClaimAsStringList("roles").
    // Filter ACCOUNTS by customerId for account holders.
    // Return the full list for tellers and auditors.
    @GetMapping("/mine")
    public List<Account> getMyAccounts(@AuthenticationPrincipal Jwt jwt) {
        // TODO 11: Read the caller's subject (customer ID, employee ID, or auditor ID)
        //          and roles list. If "teller" or "auditor" is in the roles, return
        //          ACCOUNTS in full. Otherwise filter to accounts where
        //          customerId equals the subject.
        String subject = jwt.getSubject();
        List<String> roles = jwt.getClaimAsStringList("roles");
        if (roles != null && (roles.contains("teller") || roles.contains("auditor"))) {
            return transferService.listAccounts();
        } else {
            return transferService.listAccounts().stream()
                    .filter(account -> account.customerId().equals(subject))
                    .toList();
        }
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