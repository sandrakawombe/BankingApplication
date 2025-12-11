package src.main.com.banking.transactionservice.service;

import com.banking.transactionservice.model.dto.*;
import com.banking.transactionservice.model.entity.Transaction;

import java.time.LocalDateTime;
import java.util.List;

public interface TransactionService {
    
    TransactionResponse deposit(DepositRequest request);
    
    TransactionResponse withdrawal(WithdrawalRequest request);
    
    TransactionResponse transfer(TransferRequest request);
    
    TransactionResponse getTransactionById(Long transactionId);
    
    TransactionResponse getTransactionByReference(String transactionReference);
    
    List<TransactionResponse> getTransactionsByAccountId(Long accountId);
    
    List<TransactionResponse> getTransactionsByAccountIdAndDateRange(
            Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    
    List<TransactionResponse> getAllTransactions();
}