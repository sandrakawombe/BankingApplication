// ============================================================================
// ACCOUNT NUMBER GENERATOR UTILITY
// ============================================================================

// File: AccountNumberGenerator.java
package com.banking.accountservice.util;
import org.springframework.stereotype.Component;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Utility class to generate unique account numbers
 * Format: BANKYYYYMMDDXXXXXX (20 characters)
 * Example: BANK20241210123456
 */
@Component
public class AccountNumberGenerator {

    private static final String PREFIX = "BANK";
    private static final SecureRandom random = new SecureRandom();

    /**
     * Generate a unique account number
     */
    public String generateAccountNumber() {
        // Get current date
        String datePart = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        // Generate random 6-digit number
        int randomPart = 100000 + random.nextInt(900000);

        // Combine: BANK + YYYYMMDD + XXXXXX
        return PREFIX + datePart + randomPart;
    }
}


