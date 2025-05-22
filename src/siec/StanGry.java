package siec;

import java.io.Serializable;
import java.util.List;

public class StanGry implements Serializable {
    private static final long serialVersionUID = 1L;
    public List<DaneKaczki> kaczki;
    public int wynikGracza1;
    public int wynikGracza2;
    public boolean graZakonczona;

    public StanGry(List<DaneKaczki> kaczki, int wynikGracza1, int wynikGracza2, boolean graZakonczona) {
        this.kaczki = kaczki;
        this.wynikGracza1 = wynikGracza1;
        this.wynikGracza2 = wynikGracza2;
        this.graZakonczona = graZakonczona;
    }
}
