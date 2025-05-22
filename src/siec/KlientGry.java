package siec;

import gui.OknoGry;

import java.io.*;
import java.net.*;
import java.util.function.Consumer;

public class KlientGry {
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;
    private final Consumer<StanGry> onStanGryOdebrany;
    private String imieGracza; // dodaj pole w KlientGry

    private int idGracza;
    public KlientGry(String host, int port, String imieGracza, Consumer<StanGry> onStanGryOdebrany) throws IOException {
        this.imieGracza = imieGracza;
        this.socket = new Socket(host, port);
        this.out = new ObjectOutputStream(socket.getOutputStream());
        this.in = new ObjectInputStream(socket.getInputStream());
        this.onStanGryOdebrany = onStanGryOdebrany;

        try {
            Object id = in.readObject();
            if (id instanceof Integer) {
                idGracza = (Integer) id;
                System.out.println("Otrzymano ID gracza: " + idGracza);

                // Wyślij imię
                out.writeObject(new ImieGracza(idGracza, imieGracza));
                out.flush();
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        startOdbior();
    }

    private void startOdbior() {
        Thread odbiorca = new Thread(() -> {
            try {
                while (true) {
                    Object obj = in.readObject();
                    if (obj instanceof StanGry stan) {
                        onStanGryOdebrany.accept(stan);
                    }
                }
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("Rozłączono z serwerem.");
            }
        });
        odbiorca.setDaemon(true);
        odbiorca.start();
    }

    public void wyslijStrzal(int x, int y) {
        try {
            DaneStrzalu strzal = new DaneStrzalu(idGracza, x, y);
            out.writeObject(strzal);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ustawIdGracza(int id) {
        this.idGracza = id;
    }

    public void wyslijGotowosc() {
        try {
            out.writeObject(new GotowoscGracza(idGracza));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getIdGracza() {
        return idGracza;
    }


}
