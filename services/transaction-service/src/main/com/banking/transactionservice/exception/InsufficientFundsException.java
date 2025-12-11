package src.main.com.banking.transactionservice.exception;

import java.math.BigDecimal;

public class InsufficientFundsException extends RuntimeException {
    public InsufficientFundsException(String message) {
        super(message);
    }
    
    public InsufficientFundsException(BigDecimal availableBalance, BigDecimal requestedAmount) {
        super(String.format("Insufficient funds. Available: %s, Requested: %s", 
                availableBalance, requestedAmount));
    }
}