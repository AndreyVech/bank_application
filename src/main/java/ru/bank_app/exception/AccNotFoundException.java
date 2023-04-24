package ru.bank_app.exception;

public class AccNotFoundException extends RuntimeException {
    public AccNotFoundException(Long id) {
        super("Account not found: " + id);
    }
}

