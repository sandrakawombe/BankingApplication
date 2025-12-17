package com.banking.accountservice.model.dto;

import com.banking.accountservice.model.entity.Account.AccountStatus;
import com.banking.accountservice.model.entity.Account.AccountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Response DTO for account details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {

    private Long id;
    private Long userId;
    private String accountNumber;
    private AccountType accountType;
    private String currency;
    private BigDecimal balance;
    private AccountStatus status;
    private String branchCode;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}