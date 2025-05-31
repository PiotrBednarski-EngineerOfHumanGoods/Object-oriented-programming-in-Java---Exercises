package com.stockmarket.market;

/**
 * Interfejs definiujący kontrakt dla obiektów, którymi można handlować na giełdzie.
 */
public interface Tradable {
    
    String getSymbol();
    double getCurrentPrice();
}