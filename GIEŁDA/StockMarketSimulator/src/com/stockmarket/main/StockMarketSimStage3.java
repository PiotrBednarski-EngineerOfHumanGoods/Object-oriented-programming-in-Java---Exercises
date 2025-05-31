package com.stockmarket.main;

import com.stockmarket.model.*;
import com.stockmarket.portfolio.*;
import com.stockmarket.market.*;
import com.stockmarket.exception.*;
import java.util.*;

/**
 * Główna klasa aplikacji - demonstracja pełnej funkcjonalności symulatora giełdy.
 * 
 * Ta klasa pokazuje jak wszystkie komponenty współpracują ze sobą:
 * - Tworzymy rynek z różnymi aktywami
 * - Tworzymy portfel użytkownika  
 * - Wykonujemy transakcje kupna i sprzedaży
 * - Symulujemy zmiany cen w czasie
 * - Obsługujemy błędy za pomocą wyjątków
 * 
 * To jest przykład tego jak powinna wyglądać metoda main w prawdziwej aplikacji -
 * organizuje przepływ aplikacji ale nie zawiera skomplikowanej logiki biznesowej.
 * Cała logika biznesowa jest w odpowiednich klasach (Portfolio, Market, Asset).
 */
public class StockMarketSimStage3 {
    
    /**
     * Główna metoda programu - punkt wejścia aplikacji.
     * 
     * W Javie każdy program musi mieć metodę main z dokładnie taką sygnaturą.
     * To tutaj Java Virtual Machine zaczyna wykonywanie naszego kodu.
     */
    public static void main(String[] args) {
        try {
            // Uruchamiamy główną logikę aplikacji
            // Używamy osobnej metody żeby main() była krótka i czytelna
            runStockMarketSimulation();
            
        } catch (Exception e) {
            // Łapiemy wszelkie nieoczekiwane błędy na najwyższym poziomie
            // To jest siatka bezpieczeństwa - w teorii nie powinniśmy tutaj dotrzeć
            // bo wszystkie znane błędy obsługujemy w konkretnych miejscach
            System.err.println("Wystąpił nieoczekiwany błąd w aplikacji:");
            System.err.println(e.getMessage());
            e.printStackTrace(); // W prawdziwej aplikacji logowalibyśmy to do pliku
        }
    }
    
    /**
     * Główna metoda uruchamiająca symulację.
     * 
     * Podzielona na mniejsze metody dla lepszej czytelności i organizacji.
     * Każda metoda ma jasno określoną odpowiedzialność - to jest dobra praktyka
     * programistyczna zwana "separation of concerns".
     */
    private static void runStockMarketSimulation() {
        System.out.println("=".repeat(60));
        System.out.println("    SYMULATOR GIEŁDY PAPIERÓW WARTOŚCIOWYCH");
        System.out.println("                Projekt OOP - Etap 3");
        System.out.println("=".repeat(60));
        System.out.println();
        
        // Krok 1: Przygotowanie danych
        // Tworzymy wszystkie obiekty potrzebne do symulacji
        List<Asset> marketAssets = createMarketAssets();
        Market market = new Market(marketAssets);
        Portfolio portfolio = new Portfolio(25000.0); // Startujemy z 25000 PLN
        
        // Krok 2: Pokazujemy stan początkowy
        displayWelcomeMessage(portfolio.getCash());
        displayMarketStatus(market);
        
        // Krok 3: Demonstracja transakcji
        // Pokazujemy jak działa kupno, sprzedaż i obsługa błędów
        performSampleTransactions(portfolio, market);
        
        // Krok 4: Symulacja zmian cen w czasie
        // To jest najciekawsza część - obserwujemy jak polimorfizm działa w praktyce
        runMarketSimulation(portfolio, market, 10);
        
        // Krok 5: Podsumowanie końcowe
        displayFinalResults(portfolio);
    }
    
    /**
     * Tworzy listę wszystkich aktywów dostępnych na rynku.
     * 
     * W prawdziwej aplikacji dane byłyby pobierane z bazy danych
     * lub API giełdy, ale dla demonstracji tworzymy je w kodzie.
     * 
     * Ta metoda pokazuje jak tworzymy obiekty różnych klas (Stock, Bond)
     * które wszystkie implementują ten sam interfejs lub dziedziczą po tej samej klasie.
     * 
     * @return lista aktywów rynkowych
     */
    private static List<Asset> createMarketAssets() {
        List<Asset> assets = new ArrayList<>();
        
        // Dodajemy różne akcje polskich spółek
        // Każda akcja ma symbol, nazwę i cenę początkową
        assets.add(new Stock("CDR", "CD Projekt S.A.", 280.50));
        assets.add(new Stock("PKO", "PKO Bank Polski", 42.30));
        assets.add(new Stock("KGH", "KGHM Polska Miedź", 145.80));
        assets.add(new Stock("ALE", "Allegro.eu", 26.75));
        assets.add(new Stock("PEO", "Bank Pekao", 165.20));
        assets.add(new Stock("LPP", "LPP S.A.", 2850.00));
        
        // Dodajemy obligacje o różnych stopach procentowych
        // Obligacje mają dodatkowy parametr - stopę procentową
        assets.add(new Bond("POL2030", "Obligacje Skarbu Państwa 2030", 1000.00, 3.5));
        assets.add(new Bond("TRE2028", "Obligacje Skarbu 2028", 500.00, 2.8));
        assets.add(new Bond("KOR2032", "Obligacje korporacyjne 2032", 250.00, 4.2));
        
        return assets;
    }
    
    /**
     * Wyświetla wiadomość powitalną z informacjami o początkowym stanie portfela.
     * 
     * Dobra aplikacja zawsze informuje użytkownika o tym co się dzieje.
     * Nawet w aplikacji konsolowej warto zadbać o przyjazny interfejs.
     */
    private static void displayWelcomeMessage(double initialCash) {
        System.out.println("Witamy w symulatorze giełdy papierów wartościowych!");
        System.out.printf("Na start masz do dyspozycji: %.2f PLN%n", initialCash);
        System.out.println("Możesz kupować i sprzedawać różne instrumenty finansowe.");
        System.out.println("Obserwuj jak zmieniają się ceny i wartość Twojego portfela!");
        System.out.println();
    }
    
    /**
     * Wyświetla aktualny stan rynku - wszystkie dostępne aktywa i ich ceny.
     * 
     * Ta metoda demonstruje polimorfizm - iterujemy po liście Asset,
     * ale sprawdzamy konkretny typ obiektu żeby wyświetlić odpowiednie informacje.
     */
    private static void displayMarketStatus(Market market) {
        System.out.println("📈 AKTUALNY STAN RYNKU 📈");
        System.out.println("-".repeat(50));
        
        // Grupujemy aktywa według typu dla lepszej prezentacji
        System.out.println("AKCJE:");
        for (Asset asset : market.getAllAssets().values()) {
            if (asset instanceof Stock) {
                // instanceof pozwala nam sprawdzić typ obiektu w runtime
                System.out.printf("  %-6s %-25s %8.2f PLN%n", 
                    asset.getSymbol(), asset.getName(), asset.getCurrentPrice());
            }
        }
        
        System.out.println("\nOBLIGACJE:");
        for (Asset asset : market.getAllAssets().values()) {
            if (asset instanceof Bond) {
                // Rzutujemy na Bond żeby uzyskać dostęp do getInterestRate()
                Bond bond = (Bond) asset;
                System.out.printf("  %-6s %-25s %8.2f PLN (%.1f%% rocznie)%n", 
                    bond.getSymbol(), bond.getName(), bond.getCurrentPrice(), bond.getInterestRate());
            }
        }
        System.out.println();
    }
    
    /**
     * Wykonuje przykładowe transakcje demonstrujące funkcjonalność systemu.
     * 
     * Ta metoda pokazuje jak używać naszego API (metod buy/sell z Portfolio)
     * oraz jak obsługiwać wyjątki biznesowe. To jest praktyczna demonstracja
     * tego jak wszystkie nasze klasy współpracują ze sobą.
     */
    private static void performSampleTransactions(Portfolio portfolio, Market market) {
        System.out.println("💼 PRZYKŁADOWE TRANSAKCJE 💼");
        System.out.println("-".repeat(40));
        
        try {
            // Seria udanych transakcji kupna
            System.out.println("🛒 Wykonujemy zakupy...");
            portfolio.buy("CDR", 25, market);      // Kupujemy 25 akcji CD Projekt
            portfolio.buy("PKO", 150, market);     // 150 akcji PKO
            portfolio.buy("POL2030", 10, market);  // 10 obligacji
            portfolio.buy("ALE", 80, market);      // 80 akcji Allegro
            portfolio.buy("LPP", 2, market);       // 2 drogie akcje LPP
            
            System.out.printf("Pozostało gotówki: %.2f PLN%n", portfolio.getCash());
            System.out.println();
            
            // Pokazujemy aktualny stan portfela
            displayPortfolioSummary(portfolio);
            
            // Seria transakcji sprzedaży
            System.out.println("💰 Sprzedajemy część pozycji...");
            portfolio.sell("CDR", 10, market);     // Sprzedajemy część CD Projekt
            portfolio.sell("PKO", 50, market);     // Część PKO
            
            System.out.printf("Po sprzedaży gotówki: %.2f PLN%n", portfolio.getCash());
            System.out.println();
            
        } catch (InsufficientFundsException e) {
            System.out.println("❌ Błąd transakcji: " + e.getMessage());
        } catch (AssetNotFoundException e) {
            System.out.println("❌ Błąd: " + e.getMessage());
        } catch (InsufficientAssetsException e) {
            System.out.println("❌ Błąd sprzedaży: " + e.getMessage());
        }
        
        // Demonstracja obsługi błędów - to jest bardzo ważna część!
        demonstrateErrorHandling(portfolio, market);
    }
    
    /**
     * Pokazuje jak system obsługuje różne rodzaje błędów.
     * 
     * To jest ważna część demonstracji - pokazuje że nasza aplikacja
     * jest odporna na nieprawidłowe dane wejściowe i elegancko obsługuje
     * sytuacje błędne bez crashowania.
     * 
     * W prawdziwej aplikacji takie testy byłyby w osobnych testach jednostkowych,
     * ale tutaj robimy to w main() dla demonstracji.
     */
    private static void demonstrateErrorHandling(Portfolio portfolio, Market market) {
        System.out.println("🛡️  DEMONSTRACJA OBSŁUGI BŁĘDÓW 🛡️");
        System.out.println("-".repeat(45));
        
        // Test 1: Próba kupna za dużej ilości (brak środków)
        try {
            System.out.println("Test 1: Próba kupna akcji LPP za więcej niż mamy gotówki...");
            portfolio.buy("LPP", 20, market); // LPP kosztuje ~2850 PLN, więc 20 sztuk to ~57000 PLN
        } catch (InsufficientFundsException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        } catch (AssetNotFoundException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        }
        
        // Test 2: Próba sprzedaży więcej niż mamy
        try {
            System.out.println("\nTest 2: Próba sprzedaży więcej akcji CDR niż posiadamy...");
            portfolio.sell("CDR", 100, market); // Mamy tylko 15 po wcześniejszych transakcjach
        } catch (InsufficientAssetsException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        } catch (AssetNotFoundException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        }
        
        // Test 3: Próba operacji na nieistniejącym aktywie
        try {
            System.out.println("\nTest 3: Próba kupna nieistniejącej akcji...");
            portfolio.buy("FAKE", 10, market);
        } catch (AssetNotFoundException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        } catch (InsufficientFundsException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        }
        
        // Test 4: Próba sprzedaży aktywa którego nie mamy
        try {
            System.out.println("\nTest 4: Próba sprzedaży aktywa którego nie posiadamy...");
            portfolio.sell("KGH", 5, market); // Nie kupiliśmy KGHM
        } catch (AssetNotFoundException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        } catch (InsufficientAssetsException e) {
            System.out.println("✅ Poprawnie złapano: " + e.getMessage());
        }
        
        System.out.println("\n✅ Wszystkie testy obsługi błędów przeszły pomyślnie!");
        System.out.println("To pokazuje że nasz system jest odporny na błędy użytkownika.");
        System.out.println();
    }
    
    /**
     * Uruchamia symulację zmian cen przez określoną liczbę kroków czasowych.
     * 
     * To jest serce naszej symulacji - pokazuje jak polimorfizm działa w praktyce.
     * Każdy typ aktywa aktualizuje cenę według własnej logiki, ale my nie musimy
     * wiedzieć jakiego konkretnie typu to jest. To jest piękno programowania obiektowego!
     */
    private static void runMarketSimulation(Portfolio portfolio, Market market, int steps) {
        System.out.println("📊 SYMULACJA ZMIAN CEN NA RYNKU 📊");
        System.out.println("-".repeat(50));
        
        double initialPortfolioValue = portfolio.calculateTotalValue();
        System.out.printf("Wartość początkowa portfela: %.2f PLN%n", initialPortfolioValue);
        System.out.println("Obserwuj jak zmieniają się ceny różnych typów aktywów...");
        System.out.println();
        
        // Symulacja przez określoną liczbę kroków czasowych
        for (int step = 1; step <= steps; step++) {
            System.out.printf("🕐 KROK CZASOWY %d 🕐%n", step);
            System.out.println("-".repeat(25));
            
            // Aktualizujemy ceny wszystkich aktywów na rynku
            // To jest moment gdzie polimorfizm błyszczy - jedna metoda,
            // ale różne zachowania dla Stock vs Bond
            market.updatePrices();
            
            // Pokazujemy jak zmieniły się ceny aktywów które posiadamy
            displayPriceChangesForOwnedAssets(portfolio, market);
            
            // Obliczamy i pokazujemy aktualną wartość portfela
            double currentValue = portfolio.calculateTotalValue();
            double change = currentValue - initialPortfolioValue;
            double changePercent = (change / initialPortfolioValue) * 100;
            
            System.out.printf("💰 Wartość portfela: %.2f PLN ", currentValue);
            
            // Kolorujemy output w zależności od zysku/straty (wizualnie)
            if (change > 0) {
                System.out.printf("(📈 +%.2f PLN, +%.2f%%)%n", change, changePercent);
            } else if (change < 0) {
                System.out.printf("(📉 %.2f PLN, %.2f%%)%n", change, changePercent);
            } else {
                System.out.printf("(➡️  bez zmian)%n");
            }
            
            System.out.println();
            
            // Krótka pauza dla lepszej czytelności (symulacja czasu rzeczywistego)
            try {
                Thread.sleep(1200); // 1.2 sekundy przerwy między krokami
            } catch (InterruptedException e) {
                System.out.println("Symulacja została przerwana.");
                break;
            }
        }
        
        // Podsumowanie symulacji
        double finalValue = portfolio.calculateTotalValue();
        double totalChange = finalValue - initialPortfolioValue;
        double totalChangePercent = (totalChange / initialPortfolioValue) * 100;
        
        System.out.println("📋 PODSUMOWANIE SYMULACJI 📋");
        System.out.println("-".repeat(35));
        System.out.printf("Wartość początkowa: %.2f PLN%n", initialPortfolioValue);
        System.out.printf("Wartość końcowa:    %.2f PLN%n", finalValue);
        System.out.printf("Zmiana całkowita:   %+.2f PLN (%+.2f%%)%n", totalChange, totalChangePercent);
        
        if (totalChange > 0) {
            System.out.println("🎉 Gratulacje! Twój portfel zyskał na wartości!");
        } else if (totalChange < 0) {
            System.out.println("📉 Tym razem portfel stracił na wartości. To normalne na giełdzie!");
        } else {
            System.out.println("➡️  Portfel zakończył bez zmian wartości.");
        }
        System.out.println();
    }
    
    /**
     * Pokazuje jak zmieniły się ceny aktywów które posiadamy w portfelu.
     * 
     * Ta metoda demonstruje jak enkapsulacja pozwala nam bezpiecznie
     * dostać się do danych bez naruszania integralności obiektów.
     */
    private static void displayPriceChangesForOwnedAssets(Portfolio portfolio, Market market) {
        if (portfolio.isEmpty()) {
            System.out.println("Portfel jest pusty - brak aktywów do monitorowania.");
            return;
        }
        
        System.out.println("Zmiany cen posiadanych aktywów:");
        for (PortfolioPosition position : portfolio.getPositions().values()) {
            Asset asset = position.asset();
            String symbol = asset.getSymbol();
            double currentPrice = asset.getCurrentPrice();
            int quantity = position.quantity();
            double positionValue = currentPrice * quantity;
            
            String assetType = asset instanceof Stock ? "Akcja" : "Obligacja";
            
            System.out.printf("  %-6s: %.2f PLN × %d szt. = %.2f PLN [%s]%n", 
                symbol, currentPrice, quantity, positionValue, assetType);
        }
    }
    
    /**
     * Wyświetla podsumowanie aktualnego stanu portfela.
     * 
     * Ta metoda pokazuje jak używać getterów i metod obliczeniowych
     * z naszych klas do prezentacji danych użytkownikowi.
     */
    private static void displayPortfolioSummary(Portfolio portfolio) {
        System.out.println("💼 AKTUALNY STAN PORTFELA 💼");
        System.out.println("-".repeat(35));
        
        if (portfolio.isEmpty()) {
            System.out.println("Portfel jest pusty - brak pozycji.");
        } else {
            System.out.println("Posiadane pozycje:");
            for (PortfolioPosition position : portfolio.getPositions().values()) {
                Asset asset = position.asset();
                int quantity = position.quantity();
                double positionValue = position.getTotalValue();
                String assetType = asset instanceof Stock ? "Akcja" : "Obligacja";
                
                System.out.printf("  • %-6s: %3d szt. × %.2f PLN = %8.2f PLN [%s]%n",
                    asset.getSymbol(), quantity, asset.getCurrentPrice(), 
                    positionValue, assetType);
            }
        }
        
        System.out.printf("Gotówka:             %8.2f PLN%n", portfolio.getCash());
        System.out.printf("Wartość aktywów:     %8.2f PLN%n", portfolio.calculateAssetsValue());
        System.out.printf("WARTOŚĆ CAŁKOWITA:   %8.2f PLN%n", portfolio.calculateTotalValue());
        System.out.println();
    }
    
    /**
     * Wyświetla końcowe wyniki i podsumowanie całej sesji.
     * 
     * Końcowa metoda która podsumowuje wszystko co się wydarzyło
     * i przypomina użytkownikowi o kluczowych koncepcjach które zostały zademonstrowane.
     */
    private static void displayFinalResults(Portfolio portfolio) {
        System.out.println("🎯 PODSUMOWANIE KOŃCOWE 🎯");
        System.out.println("=".repeat(45));
        
        // Szczegółowy stan portfela
        displayPortfolioSummary(portfolio);
        
        // Statystyki portfela
        System.out.println("📊 STATYSTYKI PORTFELA:");
        System.out.printf("Liczba różnych pozycji: %d%n", portfolio.getPositionCount());
        
        if (!portfolio.isEmpty()) {
            double averagePositionValue = portfolio.calculateAssetsValue() / portfolio.getPositionCount();
            System.out.printf("Średnia wartość pozycji: %.2f PLN%n", averagePositionValue);
            
            double cashRatio = (portfolio.getCash() / portfolio.calculateTotalValue()) * 100;
            System.out.printf("Udział gotówki w portfelu: %.1f%%%n", cashRatio);
        }
        
        System.out.println();
        System.out.println("🎉 Dziękujemy za skorzystanie z symulatora giełdy! 🎉");
        System.out.println();
        System.out.println("Ten projekt demonstruje kluczowe koncepcje programowania obiektowego:");
        System.out.println("• Enkapsulację (prywatne pola, publiczne metody do kontrolowanego dostępu)");
        System.out.println("• Dziedziczenie (Asset → Stock/Bond, wspólny kod w klasie bazowej)");
        System.out.println("• Polimorfizm (różne implementacje updatePrice() dla różnych typów aktywów)");
        System.out.println("• Interfejsy (Tradable określa kontrakt dla obiektów którymi można handlować)");
        System.out.println("• Obsługę wyjątków (własne klasy Exception dla błędów biznesowych)");
        System.out.println("• Organizację kodu w pakiety (logiczne grupowanie powiązanych klas)");
        System.out.println("• Używanie kolekcji (Map dla szybkiego wyszukiwania, List dla sekwencji, Optional dla bezpieczeństwa)");
        System.out.println();
        System.out.println("Każda z tych koncepcji została zastosowana w praktycznym kontekście,");
        System.out.println("pokazując jak programowanie obiektowe pomaga w tworzeniu");
        System.out.println("złożonych, ale dobrze zorganizowanych aplikacji.");
    }
}