package siec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.function.Consumer;

public class KlientGry {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Consumer<StanGry> onStanGryOdebrany; // Funkcja do obsługi otrzymanego stanu gry

    private String imieGracza;
    private int idGracza; // ID gracza nadane przez serwer (1 lub 2)

    public KlientGry(String host, int port, String imieGracza, Consumer<StanGry> onStanGryOdebrany) throws IOException {
        this.imieGracza = imieGracza;
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.onStanGryOdebrany = onStanGryOdebrany;

        try {
            Object id = in.readObject(); // Odczyt ID gracza od serwera
            if (id instanceof Integer) {
                idGracza = (Integer) id;
                System.out.println("Otrzymano ID gracza: " + idGracza);

                // Po otrzymaniu ID, wysyłamy imię gracza do serwera
                out.writeObject(new ImieGracza(idGracza, imieGracza));
                out.flush();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace(); // Obsługa błędu deserializacji
        }

        startOdbior(); // Uruchomienie wątku odbierającego dane
    }

    private void startOdbior() {
        // Osobny wątek nasłuchuje obiektów od serwera
        Thread odbiorca = new Thread(() -> {
            try {
                while (true) {
                    Object obj = in.readObject(); // Odczytaj obiekt
                    if (obj instanceof StanGry stan) {
                        onStanGryOdebrany.accept(stan); // Przekaż stan do metody obsługującej
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Rozłączono z serwerem.");
            }
        });
        odbiorca.setDaemon(true); // Wątek daemon, zamknie się wraz z aplikacją
        odbiorca.start(); // Start odbioru
    }

    public void wyslijStrzal(int x, int y) {
        try {
            DaneStrzalu strzal = new DaneStrzalu(idGracza, x, y); // Tworzymy obiekt strzału
            out.writeObject(strzal); // Wysyłamy go do serwera
            out.flush();
        } catch (IOException e) {
            e.printStackTrace(); // Obsługa błędu sieci
        }
    }

    public void ustawIdGracza(int id) {
        this.idGracza = id; // Ustawia ID gracza (jeśli potrzeba ręcznie)
    }

    public void wyslijGotowosc() {
        try {
            out.writeObject(new GotowoscGracza(idGracza)); // Informujemy serwer, że jesteśmy gotowi
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIdGracza() {
        return idGracza; // Zwraca ID gracza
    }
}
