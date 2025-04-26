package symulator;

import java.util.Scanner;
import java.util.Map;

public class SymulatorGieldy {
    private Gielda gielda;
    private Portfel portfel;
    private Scanner scanner;
    private boolean dziala;

    public SymulatorGieldy() {
        gielda = new Gielda();
        portfel = ZarzadcaPlikow.wczytajPortfel();
        scanner = new Scanner(System.in);
        dziala = true;
    }

    public void uruchom() {
        System.out.println("--- SYMULATOR GIEŁDY ---");

        while (dziala) {
            wyswietlMenu();
            int wybor = pobierzWybor();
            wykonajWybor(wybor);
            gielda.aktualizujCeny();
        }
    }

    private void wyswietlMenu() {
        System.out.println("--- MENU ---");
        System.out.println("1. Pokaż rynek");
        System.out.println("2. Pokaż portfel");
        System.out.println("3. Kup akcje");
        System.out.println("4. Sprzedaj akcje");
        System.out.println("5. Zapisz portfel");
        System.out.println("0. Wyjście");
        System.out.print("Twój wybór: ");
    }

    private int pobierzWybor() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private void wykonajWybor(int wybor) {
        switch (wybor) {
            case 1 -> gielda.wyswietlRynek();
            case 2 -> wyswietlPortfel();
            case 3 -> kupAkcje();
            case 4 -> sprzedajAkcje();
            case 5 -> ZarzadcaPlikow.zapiszPortfel(portfel);
            case 0 -> zakoncz();
            default -> System.out.println("Nieprawidłowy wybór.");
        }
    }

    private void wyswietlPortfel() {
        System.out.printf("Gotówka: %.2f PLN%n", portfel.pobierzGotowke());
        for (Map.Entry<String, Integer> wpis : portfel.pobierzAkcje().entrySet()) {
            Akcja akcja = gielda.pobierzAkcje(wpis.getKey());
            if (akcja != null) {
                System.out.printf("%s (%s): %d szt. po %.2f PLN%n", akcja.pobierzSymbol(), akcja.pobierzNazwe(), wpis.getValue(), akcja.pobierzCene());
            }
        }
    }

    private void kupAkcje() {
        gielda.wyswietlRynek();
        System.out.print("Symbol akcji do zakupu: ");
        String symbol = scanner.nextLine().toUpperCase();
        Akcja akcja = gielda.pobierzAkcje(symbol);
        if (akcja != null) {
            System.out.print("Ilość: ");
            int ilosc = Integer.parseInt(scanner.nextLine());
            portfel.kupAkcje(akcja, ilosc);
        } else {
            System.out.println("Nie znaleziono akcji.");
        }
    }

    private void sprzedajAkcje() {
        wyswietlPortfel();
        System.out.print("Symbol akcji do sprzedaży: ");
        String symbol = scanner.nextLine().toUpperCase();
        Akcja akcja = gielda.pobierzAkcje(symbol);
        if (akcja != null) {
            System.out.print("Ilość: ");
            int ilosc = Integer.parseInt(scanner.nextLine());
            portfel.sprzedajAkcje(akcja, ilosc);
        } else {
            System.out.println("Nie znaleziono akcji.");
        }
    }

    private void zakoncz() {
        ZarzadcaPlikow.zapiszPortfel(portfel);
        dziala = false;
        System.out.println("Dziękujemy za grę!");
    }

    public static void main(String[] args) {
        new SymulatorGieldy().uruchom();
    }
}
