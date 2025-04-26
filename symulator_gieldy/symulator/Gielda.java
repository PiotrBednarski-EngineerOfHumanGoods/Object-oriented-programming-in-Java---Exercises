package symulator;

import java.util.*;

public class Gielda {
    private Map<String, Akcja> dostepneAkcje;

    public Gielda() {
        dostepneAkcje = new HashMap<>();
        inicjalizujAkcje();
    }

    private void inicjalizujAkcje() {
        dodajAkcje(new Akcja("CDR", "CD Projekt", 300.0));
        dodajAkcje(new Akcja("PKO", "PKO BP", 40.0));
        dodajAkcje(new Akcja("KGH", "KGHM", 120.0));
        dodajAkcje(new Akcja("PKN", "Orlen", 80.0));
        dodajAkcje(new Akcja("PZU", "PZU", 35.0));
    }

    public void dodajAkcje(Akcja akcja) {
        dostepneAkcje.put(akcja.pobierzSymbol(), akcja);
    }

    public Akcja pobierzAkcje(String symbol) {
        return dostepneAkcje.get(symbol);
    }

    public List<Akcja> pobierzWszystkieAkcje() {
        return new ArrayList<>(dostepneAkcje.values());
    }

    public void aktualizujCeny() {
        for (Akcja akcja : dostepneAkcje.values()) {
            akcja.aktualizujCene();
        }
    }

    public void wyswietlRynek() {
        System.out.println("--- AKTUALNE NOTOWANIA ---");
        System.out.println("Symbol	Nazwa		Cena");
        for (Akcja akcja : dostepneAkcje.values()) {
            System.out.printf("%-6s	%-12s	%.2f PLN%n", akcja.pobierzSymbol(), akcja.pobierzNazwe(), akcja.pobierzCene());
        }
    }
}
