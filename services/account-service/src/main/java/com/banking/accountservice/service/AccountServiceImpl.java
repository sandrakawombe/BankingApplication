package com.banking.accountservice.service;
import com.banking.accountservice.util.AccountNumberGenerator;
import com.banking.accountservice.exception.AccountNotFoundException;
import com.banking.accountservice.model.dto.AccountResponse;
import com.banking.accountservice.model.dto.CreateAccountRequest;
import com.banking.accountservice.model.dto.UpdateAccountRequest;
import com.banking.accountservice.model.entity.Account;
import java.math.BigDecimal;
import java.util.List;
import com.banking.accountservice.exception.InsufficientBalanceException;
import com.banking.accountservice.model.entity.Account.AccountStatus;
import com.banking.accountservice.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.stream.Collectors;

/**
 * Implementation of AccountService
 * Contains business logic for account operations
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AccountServiceImpl implements AccountService {

    private final AccountRepository accountRepository;
    private final AccountNumberGenerator accountNumberGenerator;

    @Override
    public AccountResponse createAccount(CreateAccountRequest request) {
        log.info("Creating new account for user ID: {}", request.getUserId());

        // Generate unique account number
        String accountNumber = accountNumberGenerator.generateAccountNumber();

        // Verify account number is unique (shouldn't happen, but safety check)
        while (accountRepository.existsByAccountNumber(accountNumber)) {
            log.warn("Account number collision detected, generating new number");
            accountNumber = accountNumberGenerator.generateAccountNumber();
        }

        // Create account entity
        Account account = Account.builder()
                .userId(request.getUserId())
                .accountNumber(accountNumber)
                .accountType(request.getAccountType())
                .currency(request.getCurrency())
                .balance(request.getInitialBalance())
                .status(AccountStatus.ACTIVE)
                .branchCode(request.getBranchCode())
                .build();

        // Save to database
        Account savedAccount = accountRepository.save(account);
        log.info("Account created successfully: {}", savedAccount.getAccountNumber());

        return mapToResponse(savedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountById(Long id) {
        log.debug("Fetching account by ID: {}", id);
        Account account = findAccountById(id);
        return mapToResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public AccountResponse getAccountByAccountNumber(String accountNumber) {
        log.debug("Fetching account by account number: {}", accountNumber);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with number: " + accountNumber));
        return mapToResponse(account);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AccountResponse> getAccountsByUserId(Long userId) {
        log.debug("Fetching all accounts for user ID: {}", userId);
        List<Account> accounts = accountRepository.findByUserId(userId);
        return accounts.stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public AccountResponse updateAccount(Long id, UpdateAccountRequest request) {
        log.info("Updating account ID: {}", id);
        Account account = findAccountById(id);

        // Update fields if provided
        if (request.getStatus() != null) {
            account.setStatus(request.getStatus());
        }
        if (request.getBranchCode() != null) {
            account.setBranchCode(request.getBranchCode());
        }

        Account updatedAccount = accountRepository.save(account);
        log.info("Account updated successfully: {}", updatedAccount.getAccountNumber());

        return mapToResponse(updatedAccount);
    }

    @Override
    public void deleteAccount(Long id) {
        log.info("Deleting account ID: {}", id);
        Account account = findAccountById(id);

        // Soft delete - change status to CLOSED instead of deleting
        account.setStatus(AccountStatus.CLOSED);
        accountRepository.save(account);

        log.info("Account closed successfully: {}", account.getAccountNumber());
    }

    @Override
    public AccountResponse deposit(Long id, BigDecimal amount) {
        log.info("Depositing {} to account ID: {}", amount, id);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive");
        }

        Account account = findAccountById(id);

        if (!account.isActive()) {
            throw new IllegalStateException("Cannot deposit to inactive account");
        }

        account.deposit(amount);
        Account updatedAccount = accountRepository.save(account);

        log.info("Deposit successful. New balance: {}", updatedAccount.getBalance());
        return mapToResponse(updatedAccount);
    }

    @Override
    public AccountResponse withdraw(Long id, BigDecimal amount) {
        log.info("Withdrawing {} from account ID: {}", amount, id);

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive");
        }

        Account account = findAccountById(id);

        if (!account.isActive()) {
            throw new IllegalStateException("Cannot withdraw from inactive account");
        }

        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException(
                    "Insufficient balance. Available: " + account.getBalance() +
                            ", Requested: " + amount);
        }

        account.withdraw(amount);
        Account updatedAccount = accountRepository.save(account);

        log.info("Withdrawal successful. New balance: {}", updatedAccount.getBalance());
        return mapToResponse(updatedAccount);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getBalance(Long id) {
        log.debug("Fetching balance for account ID: {}", id);
        Account account = findAccountById(id);
        return account.getBalance();
    }

    @Override
    @Transactional(readOnly = true)
    public boolean accountExists(String accountNumber) {
        return accountRepository.existsByAccountNumber(accountNumber);
    }

    // ========== PRIVATE HELPER METHODS ==========

    private Account findAccountById(Long id) {
        return accountRepository.findById(id)
                .orElseThrow(() -> new AccountNotFoundException(
                        "Account not found with ID: " + id));
    }

    private AccountResponse mapToResponse(Account account) {
        return AccountResponse.builder()
                .id(account.getId())
                .userId(account.getUserId())
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .currency(account.getCurrency())
                .balance(account.getBalance())
                .status(account.getStatus())
                .branchCode(account.getBranchCode())
                .createdAt(account.getCreatedAt())
                .updatedAt(account.getUpdatedAt())
                .build();
    }
}