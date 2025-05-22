package siec;

import java.io.Serializable;

public class DaneKaczki implements Serializable {

    private static final long serialVersionUID = 1L;

    public int id; // pola publiczne w tej i podobnych klasach dla uproszczenia kodu (brak setterów itp) oraz to Data Transfer Object
    public int x;
    public int y;
    public boolean zestrzelona;
    public String grafika;

    public DaneKaczki(int id, int x, int y, boolean zestrzelona, String grafika) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.zestrzelona = zestrzelona;
        this.grafika = grafika;
    }


}
