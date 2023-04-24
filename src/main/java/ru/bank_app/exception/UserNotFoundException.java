package ru.bank_app.exception;

import java.math.BigInteger;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(Long id){
        super("Person not found: " + id);
    }
}
