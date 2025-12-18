package com.banking.transactionservice.model.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.banking.transactionservice.model.entity.TransactionType;
import com.banking.transactionservice.model.entity.TransactionStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionResponse {
    
    private Long id;
    private String transactionReference;
    private TransactionType transactionType;
    private BigDecimal amount;
    private Long sourceAccountId;
    private Long destinationAccountId;
    private String description;
    private TransactionStatus status;
    private String failureReason;
    private BigDecimal balanceAfterTransaction;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
}