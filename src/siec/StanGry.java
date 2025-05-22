package siec;

import java.io.Serializable;
import java.util.List;
public class StanGry implements Serializable {
    public List<DaneKaczki> kaczki;
    public int wynikGracza1;
    public int wynikGracza2;
    public boolean graZakonczona;
    public String imieGracza1;
    public String imieGracza2;

    public StanGry(List<DaneKaczki> kaczki, int w1, int w2, boolean zakonczona,
                   String imie1, String imie2) {
        this.kaczki = kaczki;
        this.wynikGracza1 = w1;
        this.wynikGracza2 = w2;
        this.graZakonczona = zakonczona;
        this.imieGracza1 = imie1;
        this.imieGracza2 = imie2;
    }
}
