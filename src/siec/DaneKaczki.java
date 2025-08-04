package siec;

import java.io.Serializable;

public class DaneKaczki implements Serializable {

    public int id;
    public int x, y;    // Pozycja kaczki na planszy
    public int dx, dy;  // Prędkość kaczki (zmiana pozycji w osi X i Y)
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

    // Konstruktor kopiujący – używany np. przez serwer do tworzenia kopii obiektu kaczki
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
