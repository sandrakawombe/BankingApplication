package com.banking.accountservice.service;

import com.banking.accountservice.model.dto.AccountResponse;
import com.banking.accountservice.model.dto.CreateAccountRequest;
import com.banking.accountservice.model.dto.UpdateAccountRequest;
import com.banking.accountservice.model.entity.Account;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service interface for Account operations
 */
public interface AccountService {

    /**
     * Create a new account
     */
    AccountResponse createAccount(CreateAccountRequest request);

    /**
     * Get account by ID
     */
    AccountResponse getAccountById(Long id);

    /**
     * Get account by account number
     */
    AccountResponse getAccountByAccountNumber(String accountNumber);

    /**
     * Get all accounts for a user
     */
    List<AccountResponse> getAccountsByUserId(Long userId);

    /**
     * Update account details
     */
    AccountResponse updateAccount(Long id, UpdateAccountRequest request);

    /**
     * Delete/Close account
     */
    void deleteAccount(Long id);

    /**
     * Deposit money into account
     */
    AccountResponse deposit(Long id, BigDecimal amount);

    /**
     * Withdraw money from account
     */
    AccountResponse withdraw(Long id, BigDecimal amount);

    /**
     * Get account balance
     */
    BigDecimal getBalance(Long id);

    /**
     * Check if account exists
     */
    boolean accountExists(String accountNumber);
}

