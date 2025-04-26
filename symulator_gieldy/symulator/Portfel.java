package symulator;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Portfel implements Serializable {
    private static final long serialVersionUID = 1L;
    private double gotowka;
    private Map<String, Integer> akcje;

    public Portfel(double poczatkowaGotowka) {
        this.gotowka = poczatkowaGotowka;
        this.akcje = new HashMap<>();
    }

    public boolean kupAkcje(Akcja akcja, int ilosc) {
        if (ilosc <= 0) {
            System.out.println("Ilość musi być większa od zera.");
            return false;
        }
        double koszt = akcja.pobierzCene() * ilosc;
        if (koszt > gotowka) {
            System.out.println("Za mało gotówki.");
            return false;
        }
        gotowka -= koszt;
        akcje.put(akcja.pobierzSymbol(), akcje.getOrDefault(akcja.pobierzSymbol(), 0) + ilosc);
        System.out.println("Kupiono " + ilosc + " szt. " + akcja.pobierzSymbol());
        return true;
    }

    public boolean sprzedajAkcje(Akcja akcja, int ilosc) {
        if (ilosc <= 0) {
            System.out.println("Ilość musi być większa od zera.");
            return false;
        }
        String symbol = akcja.pobierzSymbol();
        if (!akcje.containsKey(symbol) || akcje.get(symbol) < ilosc) {
            System.out.println("Brak wystarczającej ilości akcji.");
            return false;
        }
        double wartosc = akcja.pobierzCene() * ilosc;
        gotowka += wartosc;
        int nowaIlosc = akcje.get(symbol) - ilosc;
        if (nowaIlosc == 0) {
            akcje.remove(symbol);
        } else {
            akcje.put(symbol, nowaIlosc);
        }
        System.out.println("Sprzedano " + ilosc + " szt. " + symbol);
        return true;
    }

    public double pobierzGotowke() {
        return gotowka;
    }

    public Map<String, Integer> pobierzAkcje() {
        return akcje;
    }

    public double obliczWartoscAkcji(Gielda gielda) {
        double wartosc = 0.0;
        for (var wpis : akcje.entrySet()) {
            Akcja akcja = gielda.pobierzAkcje(wpis.getKey());
            if (akcja != null) {
                wartosc += akcja.pobierzCene() * wpis.getValue();
            }
        }
        return wartosc;
    }

    public double obliczCalkowitaWartosc(Gielda gielda) {
        return pobierzGotowke() + obliczWartoscAkcji(gielda);
    }
}
