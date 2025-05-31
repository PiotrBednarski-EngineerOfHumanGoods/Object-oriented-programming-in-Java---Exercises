package com.stockmarket.market;

import com.stockmarket.model.Asset;
import java.util.*;

/**
 * Klasa reprezentująca giełdę - przechowuje wszystkie dostępne aktywa i zarządza ich cenami.
 */
public class Market {
    
    // Mapa wszystkich aktywów: klucz = symbol, wartość = obiekt Asset
    private Map<String, Asset> assetMap;
    
    /**
     * Konstruktor tworzy rynek na podstawie listy aktywów.
     */
    public Market(List<Asset> assets) {
        this.assetMap = new HashMap<>();
        
        // Przekształcamy listę w mapę dla szybszego wyszukiwania
        for (Asset asset : assets) {
            assetMap.put(asset.getSymbol(), asset);
        }
    }
    
    /**
     * Zwraca aktywo o podanym symbolu.
     */
    public Optional<Asset> getAsset(String symbol) {
        return Optional.ofNullable(assetMap.get(symbol));
    }
    
    /**
     * Aktualizuje ceny wszystkich aktywów na rynku.
     */
    public void updatePrices() {
        for (Asset asset : assetMap.values()) {
            asset.updatePrice(); // Polimorfizm w akcji!
        }
    }
    
    /**
     * Zwraca mapę wszystkich aktywów (tylko do odczytu).
     */
    public Map<String, Asset> getAllAssets() {
        return Collections.unmodifiableMap(assetMap);
    }
    
    /**
     * Sprawdza czy aktywo o danym symbolu istnieje na rynku.
     */
    public boolean hasAsset(String symbol) {
        return assetMap.containsKey(symbol);
    }
}