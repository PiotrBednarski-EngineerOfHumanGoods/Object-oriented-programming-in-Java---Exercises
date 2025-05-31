package com.stockmarket.exception;

/**
 * Wyjątek rzucany gdy próbujemy operować na aktywie które nie istnieje.
 */
public class AssetNotFoundException extends Exception {
    
    public AssetNotFoundException(String message) {
        super(message);
    }
}