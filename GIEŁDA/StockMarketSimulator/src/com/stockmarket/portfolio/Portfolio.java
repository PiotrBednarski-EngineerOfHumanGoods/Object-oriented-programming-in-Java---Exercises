package com.stockmarket.portfolio;

import com.stockmarket.model.Asset;
import com.stockmarket.market.Market;
import com.stockmarket.market.Tradable;
import com.stockmarket.exception.*;
import java.util.*;

/**
 * Klasa reprezentująca portfel inwestycyjny użytkownika.
 * To jest serce naszej aplikacji - tutaj dzieje się główna logika biznesowa.
 * 
 * Portfolio przechowuje gotówkę i wszystkie posiadane aktywa.
 * Udostępnia metody do kupna, sprzedaży i obliczania wartości portfela.
 */
public class Portfolio {
    
    private double cash;  // ilość gotówki w portfelu
    // Mapa pozycji: klucz = symbol aktywa, wartość = PortfolioPosition
    private Map<String, PortfolioPosition> positions;
    
    /**
     * Konstruktor tworzy nowy portfel z określoną gotówką startową.
     * 
     * @param initialCash początkowa ilość gotówki
     * @throws IllegalArgumentException gdy gotówka jest ujemna
     */
    public Portfolio(double initialCash) {
        if (initialCash < 0) {
            throw new IllegalArgumentException("Początkowa gotówka nie może być ujemna");
        }
        this.cash = initialCash;
        this.positions = new HashMap<>();
    }
    
    /**
     * Kupuje określoną ilość aktywa z rynku.
     * 
     * To jest kompleksowa operacja która:
     * 1. Sprawdza czy aktywo istnieje na rynku
     * 2. Weryfikuje czy można nim handlować (Tradable)
     * 3. Oblicza koszt transakcji
     * 4. Sprawdza czy mamy wystarczająco gotówki
     * 5. Wykonuje transakcję (odejmuje gotówkę, dodaje aktywa)
     * 
     * @param symbol symbol aktywa do kupienia
     * @param quantity ilość do kupienia
     * @param market rynek z którego kupujemy
     * @throws InsufficientFundsException gdy nie mamy wystarczającej gotówki
     * @throws AssetNotFoundException gdy aktywa nie ma na rynku lub nie można nim handlować
     */
    public void buy(String symbol, int quantity, Market market) 
            throws InsufficientFundsException, AssetNotFoundException {
        
        // Krok 1: Sprawdzamy czy aktywo istnieje na rynku
        Optional<Asset> assetOpt = market.getAsset(symbol);
        if (assetOpt.isEmpty()) {
            throw new AssetNotFoundException("Aktywo " + symbol + " nie istnieje na rynku");
        }
        
        Asset asset = assetOpt.get();
        
        // Krok 2: Sprawdzamy czy można tym aktywem handlować
        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo " + symbol + " nie jest dostępne do handlu");
        }
        
        // Rzutowanie na Tradable jest bezpieczne bo sprawdziliśmy instanceof
        Tradable tradableAsset = (Tradable) asset;
        double currentPrice = tradableAsset.getCurrentPrice();
        
        // Krok 3: Obliczamy całkowity koszt transakcji
        double totalCost = currentPrice * quantity;
        
        // Krok 4: Sprawdzamy czy mamy wystarczająco gotówki
        if (cash < totalCost) {
            throw new InsufficientFundsException(
                String.format("Niewystarczające środki. Potrzeba: %.2f PLN, dostępne: %.2f PLN", 
                totalCost, cash));
        }
        
        // Krok 5: Wykonujemy transakcję
        cash -= totalCost;  // odejmujemy gotówkę
        addAssetToPortfolio(asset, quantity);  // dodajemy aktywa
        
        // Wyświetlamy potwierdzenie transakcji
        System.out.printf("✓ KUPNO: %d x %s @ %.2f PLN = %.2f PLN%n", 
            quantity, symbol, currentPrice, totalCost);
    }
    
    /**
     * Sprzedaje określoną ilość aktywa na rynku.
     * 
     * Proces podobny do kupna ale w drugą stronę:
     * 1. Sprawdza czy mamy to aktywo w portfelu
     * 2. Weryfikuje czy mamy wystarczającą ilość
     * 3. Pobiera aktualną cenę z rynku
     * 4. Wykonuje transakcję (dodaje gotówkę, usuwa aktywa)
     * 
     * @param symbol symbol aktywa do sprzedania
     * @param quantity ilość do sprzedania
     * @param market rynek na którym sprzedajemy
     * @throws InsufficientAssetsException gdy nie mamy wystarczającej ilości
     * @throws AssetNotFoundException gdy nie mamy tego aktywa lub nie można nim handlować
     */
    public void sell(String symbol, int quantity, Market market) 
            throws InsufficientAssetsException, AssetNotFoundException {
        
        // Krok 1: Sprawdzamy czy mamy taką pozycję w portfelu
        if (!positions.containsKey(symbol)) {
            throw new AssetNotFoundException("Nie posiadasz aktywa " + symbol + " w portfelu");
        }
        
        PortfolioPosition position = positions.get(symbol);
        
        // Krok 2: Sprawdzamy czy mamy wystarczającą ilość do sprzedania
        if (position.quantity() < quantity) {
            throw new InsufficientAssetsException(
                String.format("Niewystarczająca ilość %s. Posiadasz: %d, próbujesz sprzedać: %d", 
                symbol, position.quantity(), quantity));
        }
        
        // Krok 3: Pobieramy aktualne aktywo z rynku (potrzebujemy świeżej ceny!)
        Optional<Asset> assetOpt = market.getAsset(symbol);
        if (assetOpt.isEmpty()) {
            throw new AssetNotFoundException("Aktywo " + symbol + " nie istnieje już na rynku");
        }
        
        Asset asset = assetOpt.get();
        if (!(asset instanceof Tradable)) {
            throw new AssetNotFoundException("Aktywo " + symbol + " nie jest już dostępne do handlu");
        }
        
        Tradable tradableAsset = (Tradable) asset;
        double currentPrice = tradableAsset.getCurrentPrice();
        
        // Krok 4: Obliczamy wartość sprzedaży
        double totalValue = currentPrice * quantity;
        
        // Krok 5: Wykonujemy transakcję
        cash += totalValue;  // dodajemy gotówkę
        removeAssetFromPortfolio(symbol, quantity);  // usuwamy aktywa
        
        System.out.printf("✓ SPRZEDAŻ: %d x %s @ %.2f PLN = %.2f PLN%n", 
            quantity, symbol, currentPrice, totalValue);
    }
    
    /**
     * Prywatna metoda pomocnicza do dodawania aktywów do portfela.
     * 
     * Jeśli już mamy to aktywo, zwiększa ilość.
     * Jeśli nie mamy, tworzy nową pozycję.
     * 
     * @param asset aktywo do dodania
     * @param quantity ilość do dodania
     */
    private void addAssetToPortfolio(Asset asset, int quantity) {
        String symbol = asset.getSymbol();
        
        if (positions.containsKey(symbol)) {
            // Mamy już to aktywo - zwiększamy ilość
            // Record jest immutable więc musimy stworzyć nowy PortfolioPosition
            PortfolioPosition currentPosition = positions.get(symbol);
            int newQuantity = currentPosition.quantity() + quantity;
            positions.put(symbol, new PortfolioPosition(asset, newQuantity));
        } else {
            // Nie mamy tego aktywa - tworzymy nową pozycję
            positions.put(symbol, new PortfolioPosition(asset, quantity));
        }
    }
    
    /**
     * Prywatna metoda pomocnicza do usuwania aktywów z portfela.
     * 
     * Jeśli sprzedajemy wszystkie sztuki, usuwa pozycję całkowicie.
     * Jeśli zostaje coś, aktualizuje ilość.
     * 
     * @param symbol symbol aktywa do usunięcia
     * @param quantity ilość do usunięcia
     */
    private void removeAssetFromPortfolio(String symbol, int quantity) {
        PortfolioPosition position = positions.get(symbol);
        int newQuantity = position.quantity() - quantity;
        
        if (newQuantity == 0) {
            // Sprzedaliśmy wszystko - usuwamy pozycję całkowicie
            positions.remove(symbol);
        } else {
            // Zostało coś - aktualizujemy ilość
            // Znów tworzymy nowy PortfolioPosition bo record jest immutable
            positions.put(symbol, new PortfolioPosition(position.asset(), newQuantity));
        }
    }
    
    /**
     * Publiczna metoda do dodawania aktywów bez sprawdzania gotówki.
     * Używana do inicjalizacji portfela lub testów.
     * 
     * @param asset aktywo do dodania
     * @param quantity ilość do dodania
     */
    public void addAsset(Asset asset, int quantity) {
        addAssetToPortfolio(asset, quantity);
    }
    
    // ========== GETTERY I METODY OBLICZENIOWE ==========
    
    /**
     * Zwraca ilość gotówki w portfelu.
     * 
     * @return gotówka jako double
     */
    public double getCash() {
        return cash;
    }
    
    /**
     * Zwraca niemodyfikowalną mapę wszystkich pozycji w portfelu.
     * 
     * @return mapa pozycji (tylko do odczytu)
     */
    public Map<String, PortfolioPosition> getPositions() {
        return Collections.unmodifiableMap(positions);
    }
    
    /**
     * Oblicza łączną wartość wszystkich aktywów w portfelu.
     * 
     * Iteruje po wszystkich pozycjach i sumuje ich wartości
     * na podstawie aktualnych cen rynkowych.
     * 
     * @return łączna wartość aktywów
     */
    public double calculateAssetsValue() {
        double totalValue = 0.0;
        
        for (PortfolioPosition position : positions.values()) {
            // getTotalValue() to metoda którą dodaliśmy do PortfolioPosition
            totalValue += position.getTotalValue();
        }
        
        return totalValue;
    }
    
    /**
     * Oblicza całkowitą wartość portfela (aktywa + gotówka).
     * 
     * @return całkowita wartość portfela
     */
    public double calculateTotalValue() {
        return calculateAssetsValue() + cash;
    }
    
    /**
     * Sprawdza czy mamy pozycję w danym aktywie.
     * 
     * @param symbol symbol aktywa do sprawdzenia
     * @return true jeśli mamy pozycję, false w przeciwnym razie
     */
    public boolean hasPosition(String symbol) {
        return positions.containsKey(symbol);
    }
    
    /**
     * Zwraca ilość danego aktywa w portfelu.
     * 
     * @param symbol symbol aktywa
     * @return ilość sztuk (0 jeśli nie mamy pozycji)
     */
    public int getAssetQuantity(String symbol) {
        PortfolioPosition position = positions.get(symbol);
        return position != null ? position.quantity() : 0;
    }
    
    /**
     * Zwraca liczbę różnych typów aktywów w portfelu.
     * 
     * @return liczba unikalnych pozycji
     */
    public int getPositionCount() {
        return positions.size();
    }
    
    /**
     * Sprawdza czy portfel jest pusty (brak pozycji).
     * 
     * @return true jeśli nie ma żadnych pozycji
     */
    public boolean isEmpty() {
        return positions.isEmpty();
    }
}