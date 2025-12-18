// File: AccountNotFoundException.java
package com.banking.accountservice.exception;

public class AccountNotFoundException extends RuntimeException {
    public AccountNotFoundException(String message) {
        super(message);
    }
}