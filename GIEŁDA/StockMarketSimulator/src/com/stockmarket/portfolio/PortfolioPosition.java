package com.stockmarket.portfolio;

import com.stockmarket.model.Asset;

/**
 * Record reprezentujący pozycję w portfelu - aktywo i jego ilość.
 * Record automatycznie tworzy konstruktor, gettery, equals, hashCode i toString.
 */
public record PortfolioPosition(Asset asset, int quantity) {
    
    /**
     * Compact constructor - pozwala na walidację bez pisania pełnego konstruktora.
     */
    public PortfolioPosition {
        if (asset == null) {
            throw new IllegalArgumentException("Asset w pozycji nie może być null");
        }
        if (quantity <= 0) {
            throw new IllegalArgumentException("Ilość w pozycji musi być dodatnia");
        }
    }
    
    /**
     * Oblicza całkowitą wartość tej pozycji (cena × ilość).
     */
    public double getTotalValue() {
        return asset.getCurrentPrice() * quantity;
    }
}