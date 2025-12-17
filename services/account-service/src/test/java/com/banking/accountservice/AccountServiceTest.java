// ============================================================================
// UNIT TEST - AccountServiceTest.java
// ============================================================================

package com.banking.accountservice;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.exception.InsufficientBalanceException;
import com.banking.accountservice.model.dto.AccountResponse;
import com.banking.accountservice.model.dto.CreateAccountRequest;
import com.banking.accountservice.model.entity.Account;
import com.banking.accountservice.model.entity.Account.AccountStatus;
import com.banking.accountservice.model.entity.Account.AccountType;
import com.banking.accountservice.repository.AccountRepository;
import com.banking.accountservice.service.AccountServiceImpl;
import com.banking.accountservice.util.AccountNumberGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AccountServiceTest {

    @Mock
    private AccountRepository accountRepository;

    @Mock
    private AccountNumberGenerator accountNumberGenerator;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account testAccount;
    private CreateAccountRequest createRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        testAccount = Account.builder()
                .id(1L)
                .userId(100L)
                .accountNumber("BANK20241210123456")
                .accountType(AccountType.SAVINGS)
                .currency("USD")
                .balance(BigDecimal.valueOf(1000))
                .status(AccountStatus.ACTIVE)
                .build();

        createRequest = CreateAccountRequest.builder()
                .userId(100L)
                .accountType(AccountType.SAVINGS)
                .currency("USD")
                .initialBalance(BigDecimal.valueOf(1000))
                .build();
    }

    @Test
    void createAccount_Success() {
        // Given
        when(accountNumberGenerator.generateAccountNumber())
                .thenReturn("BANK20241210123456");
        when(accountRepository.existsByAccountNumber(any())).thenReturn(false);
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse response = accountService.createAccount(createRequest);

        // Then
        assertNotNull(response);
        assertEquals("BANK20241210123456", response.getAccountNumber());
        assertEquals(BigDecimal.valueOf(1000), response.getBalance());
        verify(accountRepository, times(1)).save(any(Account.class));
    }

    @Test
    void getAccountById_Success() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // When
        AccountResponse response = accountService.getAccountById(1L);

        // Then
        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("BANK20241210123456", response.getAccountNumber());
    }

    @Test
    void getAccountById_NotFound() {
        // Given
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(AccountNotFoundException.class, () -> {
            accountService.getAccountById(999L);
        });
    }

    @Test
    void deposit_Success() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse response = accountService.deposit(1L, BigDecimal.valueOf(500));

        // Then
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(1500), response.getBalance());
    }

    @Test
    void withdraw_Success() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        AccountResponse response = accountService.withdraw(1L, BigDecimal.valueOf(500));

        // Then
        assertNotNull(response);
        assertEquals(BigDecimal.valueOf(500), response.getBalance());
    }

    @Test
    void withdraw_InsufficientBalance() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));

        // When & Then
        assertThrows(InsufficientBalanceException.class, () -> {
            accountService.withdraw(1L, BigDecimal.valueOf(2000));
        });
    }

    @Test
    void deleteAccount_Success() {
        // Given
        when(accountRepository.findById(1L)).thenReturn(Optional.of(testAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(testAccount);

        // When
        accountService.deleteAccount(1L);

        // Then
        verify(accountRepository, times(1)).save(any(Account.class));
        assertEquals(AccountStatus.CLOSED, testAccount.getStatus());
    }
}


