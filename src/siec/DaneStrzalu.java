package siec;

import java.io.Serializable;

public class DaneStrzalu implements Serializable {
    private static final long serialVersionUID = 1L;

    public int graczId;
    public int x;          // współrzędne kliknięcia
    public int y;

    public DaneStrzalu(int graczId, int x, int y) {
        this.graczId = graczId;
        this.x = x;
        this.y = y;
    }

}
