package siec;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.LinkedBlockingQueue;

public class SerwerGry {

    private final List<DaneKaczki> kaczki = new ArrayList<>();
    private final int SZEROKOSC = 800;
    private final int WYSOKOSC = 600;
    private final int LICZBA_KACZEK = 5;

    private final List<ObjectOutputStream> wyjscia = new CopyOnWriteArrayList<>();
    private final BlockingQueue<DaneStrzalu> kolejkaStrzalow = new LinkedBlockingQueue<>();
    private int wynikGracza1 = 0;
    private int wynikGracza2 = 0;
    private volatile int gotowiGracze = 0;
    private volatile boolean graRozpoczeta = false;

    private String imieGracza1 = "Gracz A"; // Domyślne imię gracza 1
    private String imieGracza2 = "Gracz B"; // Domyślne imię gracza 2

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);
        System.out.println("Serwer nasłuchuje na porcie 5555...");

        // Oczekiwanie na połączenie dwóch graczy
        for (int i = 1; i <= 2; i++) {
            Socket socket = serverSocket.accept(); // Akceptuj połączenie
            System.out.println("Połączył się gracz #" + i);
            new Thread(new GraczHandler(socket, i)).start(); // Utwórz wątek do obsługi gracza
        }

        // Inicjalizacja kaczek na planszy
        for (int i = 0; i < LICZBA_KACZEK; i++) {
            int id = i;
            int x = new Random().nextInt(SZEROKOSC - 128);
            int y = new Random().nextInt(WYSOKOSC - 128);
            String grafika = "leci1";
            int dx = new Random().nextInt(3) + 1;
            int dy = new Random().nextInt(3) + 1;
            kaczki.add(new DaneKaczki(id, x, y, dx, dy, false, grafika));
        }

        // Główna pętla gry
        while (true) {
            if (gotowiGracze >= 2 && !graRozpoczeta) {
                System.out.println("Obaj gracze gotowi. Start gry!");
                graRozpoczeta = true;
            }

            if (graRozpoczeta) {
                przetwarzajStrzaly();
                przesylajStanGry();
                poruszajKaczki();
            }

            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void przetwarzajStrzaly() {
        while (!kolejkaStrzalow.isEmpty()) {
            DaneStrzalu s = kolejkaStrzalow.poll(); // Pobierz pierwszy strzał z kolejki

            for (DaneKaczki k : kaczki) {
                if (!k.zestrzelona &&
                        s.x >= k.x && s.x <= k.x + 128 &&
                        s.y >= k.y && s.y <= k.y + 128) {
                    k.zestrzelona = true;
                    k.grafika = "spada";

                    // Zwiększ wynik odpowiedniego gracza
                    if (s.graczId == 1) wynikGracza1++;
                    else if (s.graczId == 2) wynikGracza2++;

                    System.out.println("Gracz " + s.graczId + " trafił kaczkę! Wyniki: G1 = " + wynikGracza1 + ", G2 = " + wynikGracza2);
                    break;
                }
            }
        }
    }

    private void przesylajStanGry() {
        boolean koniecGry = kaczki.stream().allMatch(k -> k.zestrzelona); // Sprawdź czy wszystkie kaczki zostały zestrzelone

        // Utwórz nowy obiekt stanu gry
        StanGry stan = new StanGry(kopiujKaczki(), wynikGracza1, wynikGracza2, koniecGry, imieGracza1, imieGracza2);

        // Wyślij stan gry do każdego klienta
        for (ObjectOutputStream out : wyjscia) {
            try {
                out.reset();
                out.writeObject(stan);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Jeśli gra się zakończyła, zresetuj stan serwera
        if (koniecGry && graRozpoczeta) {
            graRozpoczeta = false;
            gotowiGracze = 0;
            wynikGracza1 = 0;
            wynikGracza2 = 0;
            kaczki.clear();

            // Inicjalizuj nowe kaczki
            for (int i = 0; i < LICZBA_KACZEK; i++) {
                int id = i;
                int x = new Random().nextInt(SZEROKOSC - 128);
                int y = new Random().nextInt(WYSOKOSC - 128);
                String grafika = "leci1";
                int dx = new Random().nextInt(3) + 1;
                int dy = new Random().nextInt(3) + 1;
                kaczki.add(new DaneKaczki(id, x, y, dx, dy, false, grafika));
            }
        }
    }

    private List<DaneKaczki> kopiujKaczki() {
        List<DaneKaczki> kopia = new ArrayList<>();
        for (DaneKaczki k : kaczki) {
            kopia.add(new DaneKaczki(k)); // Utwórz kopię obiektu kaczki
        }
        return kopia;
    }

    private void poruszajKaczki() {
        for (DaneKaczki k : kaczki) {
            if (!k.zestrzelona) {
                // Poruszanie kaczek
                k.x += k.dx;
                k.y += k.dy;

                // Odbijanie od krawędzi planszy
                if (k.x <= 0 || k.x + 128 >= SZEROKOSC) {
                    k.dx = -k.dx;
                }
                if (k.y <= 0 || k.y + 128 >= WYSOKOSC) {
                    k.dy = -k.dy;
                }

                // Zapobiegaj wychodzeniu poza ekran
                k.x = Math.max(0, Math.min(k.x, SZEROKOSC - 128));
                k.y = Math.max(0, Math.min(k.y, WYSOKOSC - 128));

                // Zmieniaj grafikę lecącej kaczki co 300 ms
                k.grafika = (System.currentTimeMillis() / 300) % 2 == 0 ? "leci1" : "leci2";
            } else {
                // Spadająca kaczka
                k.y += 4;
            }
        }
    }

    private class GraczHandler implements Runnable {
        private Socket socket;
        private int graczId;

        public GraczHandler(Socket socket, int graczId) {
            this.socket = socket;
            this.graczId = graczId;
        }

        @Override
        public void run() {
            try (
                    ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
                    ObjectInputStream in = new ObjectInputStream(socket.getInputStream())
            ) {
                out.writeObject(graczId); // Wyślij ID gracza do klienta
                out.flush();

                synchronized (wyjscia) {
                    wyjscia.add(out);
                }

                while (true) {
                    Object obj = in.readObject(); // Odbierz obiekt od klienta

                    if (obj instanceof DaneStrzalu strzal) {
                        if (graRozpoczeta) {
                            kolejkaStrzalow.offer(strzal); // Dodaj strzał do kolejki
                        }
                    } else if (obj instanceof GotowoscGracza g) {
                        gotowiGracze++;
                        System.out.println("Gracz #" + g.graczId + " jest gotowy.");
                    } else if (obj instanceof ImieGracza imie) {
                        if (imie.graczId == 1) imieGracza1 = imie.imie;
                        else if (imie.graczId == 2) imieGracza2 = imie.imie;
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Gracz #" + graczId + " odłączony."); // Obsługa rozłączenia gracza
            }
        }
    }

    public static void main(String[] args) {
        try {
            new SerwerGry().start(); // Uruchomienie serwera
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
