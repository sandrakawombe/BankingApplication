package src.main.com.banking.transactionservice.model.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransferRequest {
    
    @NotNull(message = "Source account ID is required")
    @Positive(message = "Source account ID must be positive")
    private Long sourceAccountId;
    
    @NotNull(message = "Destination account ID is required")
    @Positive(message = "Destination account ID must be positive")
    private Long destinationAccountId;
    
    @NotNull(message = "Amount is required")
    @DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Digits(integer = 15, fraction = 2, message = "Invalid amount format")
    private BigDecimal amount;
    
    @Size(max = 500, message = "Description cannot exceed 500 characters")
    private String description;
}