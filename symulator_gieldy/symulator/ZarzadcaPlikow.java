package symulator;

import java.io.*;

public class ZarzadcaPlikow {
    private static final String PLIK = "portfel.dat";

    public static void zapiszPortfel(Portfel portfel) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(PLIK))) {
            oos.writeObject(portfel);
        } catch (IOException e) {
            System.out.println("Błąd zapisu: " + e.getMessage());
        }
    }

    public static Portfel wczytajPortfel() {
        File plik = new File(PLIK);
        if (!plik.exists()) {
            return new Portfel(10000);
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(PLIK))) {
            return (Portfel) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            return new Portfel(10000);
        }
    }
}
