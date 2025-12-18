package com.banking.transactionservice.service;


import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.banking.transactionservice.exception.InsufficientFundsException;
import com.banking.transactionservice.exception.InvalidTransactionException;
import com.banking.transactionservice.exception.TransactionNotFoundException;
import com.banking.transactionservice.model.dto.DepositRequest;
import com.banking.transactionservice.model.dto.TransactionResponse;
import com.banking.transactionservice.model.dto.TransferRequest;
import com.banking.transactionservice.model.dto.WithdrawalRequest;
import com.banking.transactionservice.model.entity.Transaction;
import com.banking.transactionservice.model.entity.TransactionStatus;
import com.banking.transactionservice.model.entity.TransactionType;
import com.banking.transactionservice.repository.TransactionRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionRepository transactionRepository;
    
    // Mock account balances for demonstration (in real app, call Account Service)
    private BigDecimal getAccountBalance(Long accountId) {
        // In production, this would call the Account Service
        // For now, return mock balance
        return BigDecimal.valueOf(10000.00);
    }
    
    private void updateAccountBalance(Long accountId, BigDecimal newBalance) {
        // In production, this would call the Account Service to update balance
        log.info("Updated account {} balance to {}", accountId, newBalance);
    }
    
    @Override
    @Transactional
    public TransactionResponse deposit(DepositRequest request) {
        log.info("Processing deposit: accountId={}, amount={}", request.getAccountId(), request.getAmount());
        
        // Validate amount
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Deposit amount must be greater than zero");
        }
        
        // Get current balance
        BigDecimal currentBalance = getAccountBalance(request.getAccountId());
        BigDecimal newBalance = currentBalance.add(request.getAmount());
        
        // Create transaction
        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .transactionType(TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .sourceAccountId(request.getAccountId())
                .description(request.getDescription() != null ? request.getDescription() : "Deposit")
                .status(TransactionStatus.PENDING)
                .balanceAfterTransaction(newBalance)
                .build();
        
        try {
            // Save transaction
            transaction = transactionRepository.save(transaction);
            
            // Update account balance (in production, call Account Service)
            updateAccountBalance(request.getAccountId(), newBalance);
            
            // Update transaction status
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction = transactionRepository.save(transaction);
            
            log.info("Deposit completed successfully: transactionId={}, reference={}", 
                    transaction.getId(), transaction.getTransactionReference());
            
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailureReason(e.getMessage());
            transactionRepository.save(transaction);
            throw new InvalidTransactionException("Deposit failed: " + e.getMessage());
        }
        
        return mapToResponse(transaction);
    }
    
    @Override
    @Transactional
    public TransactionResponse withdrawal(WithdrawalRequest request) {
        log.info("Processing withdrawal: accountId={}, amount={}", request.getAccountId(), request.getAmount());
        
        // Validate amount
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Withdrawal amount must be greater than zero");
        }
        
        // Get current balance
        BigDecimal currentBalance = getAccountBalance(request.getAccountId());
        
        // Check sufficient funds
        if (currentBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(currentBalance, request.getAmount());
        }
        
        BigDecimal newBalance = currentBalance.subtract(request.getAmount());
        
        // Create transaction
        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .transactionType(TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .sourceAccountId(request.getAccountId())
                .description(request.getDescription() != null ? request.getDescription() : "Withdrawal")
                .status(TransactionStatus.PENDING)
                .balanceAfterTransaction(newBalance)
                .build();
        
        try {
            // Save transaction
            transaction = transactionRepository.save(transaction);
            
            // Update account balance
            updateAccountBalance(request.getAccountId(), newBalance);
            
            // Update transaction status
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction = transactionRepository.save(transaction);
            
            log.info("Withdrawal completed successfully: transactionId={}, reference={}", 
                    transaction.getId(), transaction.getTransactionReference());
            
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailureReason(e.getMessage());
            transactionRepository.save(transaction);
            throw new InvalidTransactionException("Withdrawal failed: " + e.getMessage());
        }
        
        return mapToResponse(transaction);
    }
    
    @Override
    @Transactional
    public TransactionResponse transfer(TransferRequest request) {
        log.info("Processing transfer: from={}, to={}, amount={}", 
                request.getSourceAccountId(), request.getDestinationAccountId(), request.getAmount());
        
        // Validate
        if (request.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransactionException("Transfer amount must be greater than zero");
        }
        
        if (request.getSourceAccountId().equals(request.getDestinationAccountId())) {
            throw new InvalidTransactionException("Cannot transfer to the same account");
        }
        
        // Get source account balance
        BigDecimal sourceBalance = getAccountBalance(request.getSourceAccountId());
        
        // Check sufficient funds
        if (sourceBalance.compareTo(request.getAmount()) < 0) {
            throw new InsufficientFundsException(sourceBalance, request.getAmount());
        }
        
        BigDecimal newSourceBalance = sourceBalance.subtract(request.getAmount());
        BigDecimal destinationBalance = getAccountBalance(request.getDestinationAccountId());
        BigDecimal newDestinationBalance = destinationBalance.add(request.getAmount());
        
        // Create transaction
        Transaction transaction = Transaction.builder()
                .transactionReference(generateTransactionReference())
                .transactionType(TransactionType.TRANSFER)
                .amount(request.getAmount())
                .sourceAccountId(request.getSourceAccountId())
                .destinationAccountId(request.getDestinationAccountId())
                .description(request.getDescription() != null ? request.getDescription() : "Transfer")
                .status(TransactionStatus.PENDING)
                .balanceAfterTransaction(newSourceBalance)
                .build();
        
        try {
            // Save transaction
            transaction = transactionRepository.save(transaction);
            
            // Update both account balances
            updateAccountBalance(request.getSourceAccountId(), newSourceBalance);
            updateAccountBalance(request.getDestinationAccountId(), newDestinationBalance);
            
            // Update transaction status
            transaction.setStatus(TransactionStatus.COMPLETED);
            transaction.setCompletedAt(LocalDateTime.now());
            transaction = transactionRepository.save(transaction);
            
            log.info("Transfer completed successfully: transactionId={}, reference={}", 
                    transaction.getId(), transaction.getTransactionReference());
            
        } catch (Exception e) {
            transaction.setStatus(TransactionStatus.FAILED);
            transaction.setFailureReason(e.getMessage());
            transactionRepository.save(transaction);
            throw new InvalidTransactionException("Transfer failed: " + e.getMessage());
        }
        
        return mapToResponse(transaction);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionById(Long transactionId) {
        log.info("Fetching transaction by ID: {}", transactionId);
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new TransactionNotFoundException(transactionId));
        return mapToResponse(transaction);
    }
    
    @Override
    @Transactional(readOnly = true)
    public TransactionResponse getTransactionByReference(String transactionReference) {
        log.info("Fetching transaction by reference: {}", transactionReference);
        Transaction transaction = transactionRepository.findByTransactionReference(transactionReference)
                .orElseThrow(() -> new TransactionNotFoundException("Transaction not found with reference: " + transactionReference));
        return mapToResponse(transaction);
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccountId(Long accountId) {
        log.info("Fetching transactions for account: {}", accountId);
        List<Transaction> transactions = transactionRepository.findAllByAccountId(accountId);
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getTransactionsByAccountIdAndDateRange(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching transactions for account: {} between {} and {}", accountId, startDate, endDate);
        List<Transaction> transactions = transactionRepository.findByAccountIdAndDateRange(accountId, startDate, endDate);
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponse> getAllTransactions() {
        log.info("Fetching all transactions");
        List<Transaction> transactions = transactionRepository.findAll();
        return transactions.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }
    
    private String generateTransactionReference() {
        return "TXN-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }
    
    private TransactionResponse mapToResponse(Transaction transaction) {
        return TransactionResponse.builder()
                .id(transaction.getId())
                .transactionReference(transaction.getTransactionReference())
                .transactionType(transaction.getTransactionType())
                .amount(transaction.getAmount())
                .sourceAccountId(transaction.getSourceAccountId())
                .destinationAccountId(transaction.getDestinationAccountId())
                .description(transaction.getDescription())
                .status(transaction.getStatus())
                .failureReason(transaction.getFailureReason())
                .balanceAfterTransaction(transaction.getBalanceAfterTransaction())
                .createdAt(transaction.getCreatedAt())
                .completedAt(transaction.getCompletedAt())
                .build();
    }
}