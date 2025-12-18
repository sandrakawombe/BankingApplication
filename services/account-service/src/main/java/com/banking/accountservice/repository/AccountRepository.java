package com.banking.accountservice.repository;

import com.banking.accountservice.model.entity.Account;
import com.banking.accountservice.model.entity.Account.AccountStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository interface for Account entity
 * Spring Data JPA automatically implements these methods
 */
@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    /**
     * Find account by account number
     */
    Optional<Account> findByAccountNumber(String accountNumber);

    /**
     * Find all accounts for a specific user
     */
    List<Account> findByUserId(Long userId);

    /**
     * Find all active accounts for a user
     */
    List<Account> findByUserIdAndStatus(Long userId, AccountStatus status);

    /**
     * Check if account number already exists
     */
    boolean existsByAccountNumber(String accountNumber);

    /**
     * Find accounts by currency
     */
    List<Account> findByCurrency(String currency);

    /**
     * Find accounts by status
     */
    List<Account> findByStatus(AccountStatus status);

    /**
     * Custom query to find accounts with balance greater than specified amount
     */
    @Query("SELECT a FROM Account a WHERE a.balance > :minBalance AND a.status = :status")
    List<Account> findAccountsWithMinimumBalance(
        @Param("minBalance") java.math.BigDecimal minBalance,
        @Param("status") AccountStatus status
    );

    /**
     * Count accounts by user ID
     */
    long countByUserId(Long userId);

    /**
     * Delete accounts by user ID (for testing/admin purposes)
     */
    void deleteByUserId(Long userId);
}