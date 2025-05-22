package siec;

import java.io.*;
import java.net.*;
import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.CopyOnWriteArrayList;


public class SerwerGry {

    private final List<DaneKaczki> kaczki = new ArrayList<>();
    private final int SZEROKOSC = 800;
    private final int WYSOKOSC = 600;
    private final int LICZBA_KACZEK = 5;

    private final List<ObjectOutputStream> wyjscia = new CopyOnWriteArrayList<>();
    private final BlockingQueue<DaneStrzalu> kolejkaStrzalow = new LinkedBlockingQueue<>();

    private int wynikGracza1 = 0;
    private int wynikGracza2 = 0;

    public void start() throws IOException {
        ServerSocket serverSocket = new ServerSocket(5555);
        System.out.println("Serwer nasłuchuje na porcie 5555...");

        // Oczekiwanie na 2 graczy
        for (int i = 1; i <= 2; i++) {
            Socket socket = serverSocket.accept();
            System.out.println("Połączył się gracz #" + i);
            new Thread(new GraczHandler(socket, i)).start();
        }

        // Inicjalizacja kaczek
        for (int i = 0; i < LICZBA_KACZEK; i++) {
            int id = i;
            int x = new Random().nextInt(SZEROKOSC - 128);
            int y = new Random().nextInt(WYSOKOSC - 128);
            String grafika = "leci1";
            kaczki.add(new DaneKaczki(id, x, y, false, grafika));
        }

        // Główna pętla gry
        while (true) {
            przetwarzajStrzaly();
            przesylajStanGry();
            poruszajKaczki();
            try {
                Thread.sleep(50); // ~20 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void przetwarzajStrzaly() {
        while (!kolejkaStrzalow.isEmpty()) {
            DaneStrzalu s = kolejkaStrzalow.poll();
            for (DaneKaczki k : kaczki) {
                if (!k.zestrzelona &&
                        s.x >= k.x && s.x <= k.x + 128 &&
                        s.y >= k.y && s.y <= k.y + 128) {

                    k.zestrzelona = true;
                    k.grafika = "spada";
                    if (s.graczId == 1) wynikGracza1++;
                    else if (s.graczId == 2) wynikGracza2++;
                    break;
                }
            }
        }
    }

    private void przesylajStanGry() {
        boolean koniecGry = kaczki.stream().allMatch(k -> k.zestrzelona);
        StanGry stan = new StanGry(kopiujKaczki(), wynikGracza1, wynikGracza2, koniecGry);
        for (ObjectOutputStream out : wyjscia) {
            try {
                out.reset(); //  czyści bufor referencji
                out.writeObject(stan);
                out.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private List<DaneKaczki> kopiujKaczki() {
        List<DaneKaczki> kopia = new ArrayList<>();
        for (DaneKaczki k : kaczki) {
            kopia.add(new DaneKaczki(k.id, k.x, k.y, k.zestrzelona, k.grafika));
        }
        return kopia;
    }

    private void poruszajKaczki() {
        for (DaneKaczki k : kaczki) {
            if (!k.zestrzelona) {
                k.x += new Random().nextInt(5) - 2; // -2 do +2
                k.y += new Random().nextInt(5) - 2;
                k.x = Math.max(0, Math.min(k.x, SZEROKOSC - 128));
                k.y = Math.max(0, Math.min(k.y, WYSOKOSC - 128));
                k.grafika = (System.currentTimeMillis() / 300) % 2 == 0 ? "leci1" : "leci2";
            } else {
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
                out.writeObject(graczId); // << TU
                out.flush();

                synchronized (wyjscia) {
                    wyjscia.add(out);
                }

                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof DaneStrzalu strzal) {
                        kolejkaStrzalow.offer(strzal);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Gracz #" + graczId + " odłączony.");
            }
        }
    }


    public static void main(String[] args) {
        try {
            new SerwerGry().start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
