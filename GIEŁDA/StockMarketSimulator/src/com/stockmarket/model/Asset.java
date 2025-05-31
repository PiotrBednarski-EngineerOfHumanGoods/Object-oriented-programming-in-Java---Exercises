package com.stockmarket.model;

/**
 * Abstrakcyjna klasa bazowa reprezentująca dowolny instrument finansowy.
 */
public abstract class Asset {
    
    // protected oznacza że klasy dziedziczące mają dostęp do tych pól
    protected String symbol;        
    protected String name;          
    protected double currentPrice;  
    
    /**
     * Konstruktor klasy bazowej.
     */
    public Asset(String symbol, String name, double initialPrice) {
        // Sprawdzamy poprawność danych wejściowych
        if (symbol == null || symbol.trim().isEmpty()) {
            throw new IllegalArgumentException("Symbol aktywa nie może być pusty");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Nazwa aktywa nie może być pusta");
        }
        if (initialPrice < 0) {
            throw new IllegalArgumentException("Cena aktywa nie może być ujemna");
        }
        
        this.symbol = symbol;
        this.name = name;
        this.currentPrice = initialPrice;
    }
    
    // Metody dostępowe (gettery)
    public String getSymbol() {
        return symbol;
    }
    
    public String getName() {
        return name;
    }
    
    public double getCurrentPrice() {
        return currentPrice;
    }
    
    /**
     * Abstrakcyjna metoda - każda klasa dziedzicząca MUSI ją zaimplementować.
     */
    public abstract void updatePrice();
    
    /**
     * Sprawdza czy dwa aktywa są identyczne na podstawie symbolu.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Asset asset = (Asset) obj;
        return symbol.equals(asset.symbol);
    }
    
    /**
     * Zwraca hash code na podstawie symbolu.
     */
    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
    
    /**
     * Zwraca tekstową reprezentację aktywa.
     */
    @Override
    public String toString() {
        return String.format("%s (%s): %.2f PLN", symbol, name, currentPrice);
    }
}