package src.main.com.banking.transactionservice.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import src.main.com.banking.transactionservice.model.entity.Transaction;
import src.main.com.banking.transactionservice.model.entity.TransactionStatus;
import src.main.com.banking.transactionservice.model.entity.TransactionType;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    
    Optional<Transaction> findByTransactionReference(String transactionReference);
    
    List<Transaction> findBySourceAccountIdOrderByCreatedAtDesc(Long accountId);
    
    List<Transaction> findByDestinationAccountIdOrderByCreatedAtDesc(Long accountId);
    
    @Query("SELECT t FROM Transaction t WHERE t.sourceAccountId = :accountId OR t.destinationAccountId = :accountId ORDER BY t.createdAt DESC")
    List<Transaction> findAllByAccountId(@Param("accountId") Long accountId);
    
    List<Transaction> findByStatusOrderByCreatedAtDesc(TransactionStatus status);
    
    List<Transaction> findByTransactionTypeAndStatusOrderByCreatedAtDesc(TransactionType transactionType, TransactionStatus status);
    
    @Query("SELECT t FROM Transaction t WHERE (t.sourceAccountId = :accountId OR t.destinationAccountId = :accountId) AND t.createdAt BETWEEN :startDate AND :endDate ORDER BY t.createdAt DESC")
    List<Transaction> findByAccountIdAndDateRange(
            @Param("accountId") Long accountId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );
    
    Long countBySourceAccountIdAndStatus(Long accountId, TransactionStatus status);
}