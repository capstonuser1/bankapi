package com.example.bankapi.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AccountOwnershipTest {

    @Mock
    private AccountService accountService;

    @InjectMocks
    private AccountOwnership accountOwnership;

    @Test
    void isOwner_nullAuthentication_returnsFalse() {
        assertFalse(accountOwnership.isOwner("1", null));
    }

    @Test
    void isOwner_withAuthentication_returnsTrue() {
        Authentication auth = mock(Authentication.class);
        when(auth.getName()).thenReturn("CUST-1");

        assertTrue(accountOwnership.isOwner("1", auth));
    }
}
