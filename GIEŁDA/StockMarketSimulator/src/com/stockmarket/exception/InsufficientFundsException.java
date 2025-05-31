package com.stockmarket.exception;

/**
 * Wyjątek rzucany gdy użytkownik próbuje kupić aktywa, ale nie ma wystarczającej gotówki.
 */
public class InsufficientFundsException extends Exception {
    
    public InsufficientFundsException(String message) {
        super(message);
    }
}