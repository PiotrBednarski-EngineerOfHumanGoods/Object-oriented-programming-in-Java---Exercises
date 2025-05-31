package com.stockmarket.model;

import com.stockmarket.market.Tradable;

/**
 * Klasa reprezentująca obligacje.
 */
public class Bond extends Asset implements Tradable {
    
    private double interestRate; // stopa procentowa roczna
    
    /**
     * Konstruktor obligacji z dodatkowym parametrem dla stopy procentowej.
     */
    public Bond(String symbol, String name, double initialPrice, double interestRate) {
        super(symbol, name, initialPrice);
        this.interestRate = interestRate;
    }
    
    /**
     * Zwraca stopę procentową obligacji.
     */
    public double getInterestRate() {
        return interestRate;
    }
    
    /**
     * Obligacje rosną powoli i przewidywalnie na podstawie stopy procentowej.
     */
    @Override
    public void updatePrice() {
        // Miesięczny wzrost na podstawie rocznej stopy procentowej
        double monthlyGrowth = interestRate / 12.0 / 100.0;
        this.currentPrice = this.currentPrice * (1.0 + monthlyGrowth);
    }
}