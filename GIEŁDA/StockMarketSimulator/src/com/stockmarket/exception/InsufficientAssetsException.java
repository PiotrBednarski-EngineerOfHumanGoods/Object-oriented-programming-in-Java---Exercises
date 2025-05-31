package com.stockmarket.exception;

/**
 * Wyjątek rzucany gdy użytkownik próbuje sprzedać więcej aktywów niż posiada.
 */
public class InsufficientAssetsException extends Exception {
    
    public InsufficientAssetsException(String message) {
        super(message);
    }
}