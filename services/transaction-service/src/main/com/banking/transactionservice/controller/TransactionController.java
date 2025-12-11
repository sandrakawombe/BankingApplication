package src.main.com.banking.transactionservice.controller;

import com.banking.transactionservice.model.dto.*;
import com.banking.transactionservice.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/transactions")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Transaction API", description = "Endpoints for managing financial transactions")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @PostMapping("/deposit")
    @Operation(summary = "Deposit money", description = "Deposit money into an account")
    public ResponseEntity<TransactionResponse> deposit(@Valid @RequestBody DepositRequest request) {
        log.info("Received deposit request: {}", request);
        TransactionResponse response = transactionService.deposit(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw money", description = "Withdraw money from an account")
    public ResponseEntity<TransactionResponse> withdraw(@Valid @RequestBody WithdrawalRequest request) {
        log.info("Received withdrawal request: {}", request);
        TransactionResponse response = transactionService.withdrawal(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "Transfer money", description = "Transfer money between accounts")
    public ResponseEntity<TransactionResponse> transfer(@Valid @RequestBody TransferRequest request) {
        log.info("Received transfer request: {}", request);
        TransactionResponse response = transactionService.transfer(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get transaction by ID", description = "Retrieve a transaction by its ID")
    public ResponseEntity<TransactionResponse> getTransactionById(@PathVariable Long id) {
        log.info("Fetching transaction with ID: {}", id);
        TransactionResponse response = transactionService.getTransactionById(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/reference/{reference}")
    @Operation(summary = "Get transaction by reference", description = "Retrieve a transaction by its reference number")
    public ResponseEntity<TransactionResponse> getTransactionByReference(@PathVariable String reference) {
        log.info("Fetching transaction with reference: {}", reference);
        TransactionResponse response = transactionService.getTransactionByReference(reference);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/account/{accountId}")
    @Operation(summary = "Get account transactions", description = "Retrieve all transactions for an account")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactions(@PathVariable Long accountId) {
        log.info("Fetching transactions for account: {}", accountId);
        List<TransactionResponse> responses = transactionService.getTransactionsByAccountId(accountId);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/account/{accountId}/date-range")
    @Operation(summary = "Get account transactions by date range", 
               description = "Retrieve transactions for an account within a date range")
    public ResponseEntity<List<TransactionResponse>> getAccountTransactionsByDateRange(
            @PathVariable Long accountId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        log.info("Fetching transactions for account: {} between {} and {}", accountId, startDate, endDate);
        List<TransactionResponse> responses = transactionService.getTransactionsByAccountIdAndDateRange(
                accountId, startDate, endDate);
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping
    @Operation(summary = "Get all transactions", description = "Retrieve all transactions")
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        log.info("Fetching all transactions");
        List<TransactionResponse> responses = transactionService.getAllTransactions();
        return ResponseEntity.ok(responses);
    }
}