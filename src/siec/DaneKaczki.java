package siec;

import java.io.Serializable;

public class DaneKaczki implements Serializable {
    public int id;
    public int x, y;
    public int dx, dy;
    public boolean zestrzelona;
    public String grafika;

    public DaneKaczki(int id, int x, int y, int dx, int dy, boolean zestrzelona, String grafika) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.zestrzelona = zestrzelona;
        this.grafika = grafika;
    }

    // kopia konstruktora uproszczonego dla kopiowania w serwerze
    public DaneKaczki(DaneKaczki inna) {
        this.id = inna.id;
        this.x = inna.x;
        this.y = inna.y;
        this.dx = inna.dx;
        this.dy = inna.dy;
        this.zestrzelona = inna.zestrzelona;
        this.grafika = inna.grafika;
    }

}
