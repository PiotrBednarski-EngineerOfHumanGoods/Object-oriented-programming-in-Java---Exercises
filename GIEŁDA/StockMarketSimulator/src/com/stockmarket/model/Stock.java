package com.stockmarket.model;

import com.stockmarket.market.Tradable;

/**
 * Klasa reprezentująca akcje spółki.
 */
public class Stock extends Asset implements Tradable {
    
    /**
     * Konstruktor akcji.
     */
    public Stock(String symbol, String name, double initialPrice) {
        super(symbol, name, initialPrice); // Wywołujemy konstruktor klasy Asset
    }
    
    /**
     * Akcje mają zmienne ceny - mogą rosnąć i spadać losowo.
     */
    @Override
    public void updatePrice() {
        // Losowa zmiana ceny o maksymalnie +/- 10%
        double changePercent = (Math.random() - 0.5) * 0.2;
        this.currentPrice = this.currentPrice * (1.0 + changePercent);
        
        // Akcje nie mogą kosztować mniej niż 1 PLN
        if (this.currentPrice < 1.0) {
            this.currentPrice = 1.0;
        }
    }
}