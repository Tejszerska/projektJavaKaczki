package siec;

import java.io.Serializable;

public class DaneStrzalu implements Serializable {

    private static final long serialVersionUID = 1L; // Umożliwia wersjonowanie klasy przy przesyłaniu obiektu przez sieć

    public int graczId;  // ID gracza, który oddał strzał
    public int x; // Współrzędna X miejsca kliknięcia
    public int y; // Współrzędna Y miejsca kliknięcia

    public DaneStrzalu(int graczId, int x, int y) {
        this.graczId = graczId;
        this.x = x;
        this.y = y;
    }
}
