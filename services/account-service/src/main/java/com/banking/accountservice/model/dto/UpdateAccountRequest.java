package com.banking.accountservice.model.dto;

import com.banking.accountservice.model.entity.Account.AccountStatus;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request DTO for updating account details
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateAccountRequest {

    private AccountStatus status;

    @Size(max = 10, message = "Branch code cannot exceed 10 characters")
    private String branchCode;
}