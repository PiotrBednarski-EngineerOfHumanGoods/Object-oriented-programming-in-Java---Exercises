package symulator;

import java.util.Random;

// Klasa reprezentująca akcję
public class Akcja {
    private String symbol;
    private String nazwa;
    private double cena;
    private Random losowy;

    public Akcja(String symbol, String nazwa, double cena) {
        this.symbol = symbol;
        this.nazwa = nazwa;
        this.cena = cena;
        this.losowy = new Random();
    }

    public String pobierzSymbol() {
        return symbol;
    }

    public String pobierzNazwe() {
        return nazwa;
    }

    public double pobierzCene() {
        return cena;
    }

    public void aktualizujCene() {
        double zmianaProcent = (losowy.nextDouble() * 6) - 3;
        double zmianaKwota = cena * (zmianaProcent / 100.0);
        cena += zmianaKwota;
        if (cena < 0.01) {
            cena = 0.01;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Akcja akcja = (Akcja) o;
        return symbol.equals(akcja.symbol);
    }

    @Override
    public int hashCode() {
        return symbol.hashCode();
    }
}
