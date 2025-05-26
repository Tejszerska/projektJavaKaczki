package gui;

import siec.KlientGry;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OknoGry extends JFrame {
    private CardLayout cardLayout;  // Układ kart do przełączania między panelami (menu, gra, koniec)
    private JPanel cards;
    private PanelGry panelGry;
    private PanelKoniec panelKoniec;
    private String imieGracza = "";

    // Statyczna tablica przechowująca imiona graczy według ich ID (1 lub 2)
    private static final String[] imionaGraczy = new String[3]; // [0] nieużywane

    public OknoGry() throws IOException {
        setTitle("Kaczuchy Multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        MenuStartowe menuStartowe = new MenuStartowe(this);
        panelKoniec = new PanelKoniec(this);

        final PanelGry[] tempPanel = new PanelGry[1]; // Potrzebne do lambdy - musi być efektywnie finalne

        // Tworzenie klienta gry (łączy się z serwerem i nasłuchuje stan gry)
        KlientGry klient = new KlientGry("localhost", 5555, imieGracza, stan -> {
            if (tempPanel[0] != null) {
                tempPanel[0].aktualizujStan(stan); // Aktualizacja stanu gry w PanelGry
            }
        });

        panelGry = new PanelGry(klient, this);
        tempPanel[0] = panelGry;   // Przypisanie do tablicy pomocniczej

        cards.add(menuStartowe, "menu");
        cards.add(panelGry, "gra");
        cards.add(panelKoniec, "koniec");

        setContentPane(cards);
        pack();  // Dopasowanie rozmiaru okna do jego zawartości
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Ustawienie imienia gracza (przechowywane lokalnie dla klienta)
    public void ustawImieGracza(String imie) {
        this.imieGracza = imie;
    }

    // Pobranie imienia gracza lokalnego (z menu)
    public String pobierzImieGracza() {
        return imieGracza;
    }

    // Ustawienie imienia gracza dla konkretnego ID (1 lub 2)
    public void ustawImieDlaId(int id, String imie) {
        if (id >= 1 && id <= 2) {
            imionaGraczy[id] = imie;
        }
    }

    // Pobranie imienia gracza dla danego ID; jeśli brak, zwraca "Gracz X"
    public String pobierzImieDlaId(int id) {
        if (id >= 1 && id <= 2) {
            return imionaGraczy[id] != null ? imionaGraczy[id] : "Gracz " + id;
        }
        return "Gracz";
    }

    public void pokazPanel(String nazwa) {
        cardLayout.show(cards, nazwa);
    }

    public PanelKoniec pobierzPanelKoniec() {
        return panelKoniec;
    }

    public void rozpocznijNowaGre() {
        panelGry.rozpocznijNowaGre();
        pokazPanel("gra");
    }

    public void powiadomSerwerOStart() {
        panelGry.getKlient().wyslijGotowosc();
    }
}
