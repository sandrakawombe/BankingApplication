package com.banking.accountservice.controller;

import com.banking.accountservice.model.dto.AccountResponse;
import com.banking.accountservice.model.dto.CreateAccountRequest;
import com.banking.accountservice.model.dto.UpdateAccountRequest;
import com.banking.accountservice.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * REST Controller for Account Service
 * Handles all account-related HTTP requests
 * 
 * Base URL: /api/v1/accounts
 */
@RestController
@RequestMapping("/api/v1/accounts")
@RequiredArgsConstructor
@Slf4j
@Validated
@Tag(name = "Account Management", description = "APIs for managing bank accounts")
public class AccountController {

    private final AccountService accountService;

    /**
     * Create a new account
     * POST /api/v1/accounts
     */
    @PostMapping
    @Operation(summary = "Create new account", description = "Creates a new bank account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Account created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public ResponseEntity<AccountResponse> createAccount(
            @Valid @RequestBody CreateAccountRequest request) {
        log.info("Received request to create account for user ID: {}", request.getUserId());
        AccountResponse response = accountService.createAccount(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    /**
     * Get account by ID
     * GET /api/v1/accounts/{id}
     */
    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID", description = "Retrieves account details by ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Account found"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<AccountResponse> getAccountById(
            @Parameter(description = "Account ID") 
            @PathVariable @Positive Long id) {
        log.debug("Received request to get account ID: {}", id);
        AccountResponse response = accountService.getAccountById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * Get account by account number
     * GET /api/v1/accounts/number/{accountNumber}
     */
    @GetMapping("/number/{accountNumber}")
    @Operation(summary = "Get account by account number")
    public ResponseEntity<AccountResponse> getAccountByAccountNumber(
            @Parameter(description = "Account Number") 
            @PathVariable String accountNumber) {
        log.debug("Received request to get account number: {}", accountNumber);
        AccountResponse response = accountService.getAccountByAccountNumber(accountNumber);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all accounts for a user
     * GET /api/v1/accounts/user/{userId}
     */
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get accounts by user ID", description = "Retrieves all accounts for a user")
    public ResponseEntity<List<AccountResponse>> getAccountsByUserId(
            @Parameter(description = "User ID") 
            @PathVariable @Positive Long userId) {
        log.debug("Received request to get accounts for user ID: {}", userId);
        List<AccountResponse> responses = accountService.getAccountsByUserId(userId);
        return ResponseEntity.ok(responses);
    }

    /**
     * Update account details
     * PUT /api/v1/accounts/{id}
     */
    @PutMapping("/{id}")
    @Operation(summary = "Update account", description = "Updates account details")
    public ResponseEntity<AccountResponse> updateAccount(
            @Parameter(description = "Account ID") 
            @PathVariable @Positive Long id,
            @Valid @RequestBody UpdateAccountRequest request) {
        log.info("Received request to update account ID: {}", id);
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }

    /**
     * Delete/Close account
     * DELETE /api/v1/accounts/{id}
     */
    @DeleteMapping("/{id}")
    @Operation(summary = "Close account", description = "Closes/deletes an account")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Account closed successfully"),
        @ApiResponse(responseCode = "404", description = "Account not found")
    })
    public ResponseEntity<Void> deleteAccount(
            @Parameter(description = "Account ID") 
            @PathVariable @Positive Long id) {
        log.info("Received request to delete account ID: {}", id);
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deposit money
     * POST /api/v1/accounts/{id}/deposit
     */
    @PostMapping("/{id}/deposit")
    @Operation(summary = "Deposit money", description = "Deposits money into account")
    public ResponseEntity<AccountResponse> deposit(
            @Parameter(description = "Account ID") 
            @PathVariable @Positive Long id,
            @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        log.info("Received request to deposit {} to account ID: {}", amount, id);
        AccountResponse response = accountService.deposit(id, amount);
        return ResponseEntity.ok(response);
    }

    /**
     * Withdraw money
     * POST /api/v1/accounts/{id}/withdraw
     */
    @PostMapping("/{id}/withdraw")
    @Operation(summary = "Withdraw money", description = "Withdraws money from account")
    public ResponseEntity<AccountResponse> withdraw(
            @Parameter(description = "Account ID") 
            @PathVariable @Positive Long id,
            @RequestBody Map<String, BigDecimal> request) {
        BigDecimal amount = request.get("amount");
        log.info("Received request to withdraw {} from account ID: {}", amount, id);
        AccountResponse response = accountService.withdraw(id, amount);
        return ResponseEntity.ok(response);
    }

    /**
     * Get account balance
     * GET /api/v1/accounts/{id}/balance
     */
    @GetMapping("/{id}/balance")
    @Operation(summary = "Get balance", description = "Retrieves account balance")
    public ResponseEntity<Map<String, BigDecimal>> getBalance(
            @Parameter(description = "Account ID") 
            @PathVariable @Positive Long id) {
        log.debug("Received request to get balance for account ID: {}", id);
        BigDecimal balance = accountService.getBalance(id);
        return ResponseEntity.ok(Map.of("balance", balance));
    }

    /**
     * Health check endpoint
     * GET /api/v1/accounts/health
     */
    @GetMapping("/health")
    @Operation(summary = "Health check", description = "Checks if the service is running")
    public ResponseEntity<Map<String, String>> healthCheck() {
        return ResponseEntity.ok(Map.of(
            "status", "UP",
            "service", "account-service",
            "timestamp", java.time.LocalDateTime.now().toString()
        ));
    }
}