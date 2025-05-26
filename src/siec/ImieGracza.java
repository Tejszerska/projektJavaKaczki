package siec;

import java.io.Serializable;

public class ImieGracza implements Serializable { // Obiekt do przesyłania imienia gracza wraz z jego ID

    public int graczId;
    public String imie;

    public ImieGracza(int graczId, String imie) {
        this.graczId = graczId;
        this.imie = imie;
    }
}
