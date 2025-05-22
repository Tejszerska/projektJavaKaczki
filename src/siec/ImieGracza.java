package siec;

import java.io.Serializable;

public class ImieGracza implements Serializable {
    public int graczId;
    public String imie;

    public ImieGracza(int graczId, String imie) {
        this.graczId = graczId;
        this.imie = imie;
    }
}