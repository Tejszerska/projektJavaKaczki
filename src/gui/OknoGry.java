package gui;

import siec.KlientGry;
import siec.StanGry;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class OknoGry extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;
    private PanelGry panelGry;
    private PanelKoniec panelKoniec;

    private String imieGracza = "";
    private static final String[] imionaGraczy = new String[3]; // indexy 1 i 2

    public OknoGry() throws IOException {
        setTitle("Kaczuchy Multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        MenuStartowe menuStartowe = new MenuStartowe(this);
        panelKoniec = new PanelKoniec(this);

        final PanelGry[] tempPanel = new PanelGry[1];
        KlientGry klient = new KlientGry("localhost", 5555, imieGracza, stan -> {
            if (tempPanel[0] != null) {
                tempPanel[0].aktualizujStan(stan);
            }
        });

        panelGry = new PanelGry(klient, this);
        tempPanel[0] = panelGry;

        cards.add(menuStartowe, "menu");
        cards.add(panelGry, "gra");
        cards.add(panelKoniec, "koniec");

        setContentPane(cards);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void ustawImieGracza(String imie) {
        this.imieGracza = imie;
    }

    public String pobierzImieGracza() {
        return imieGracza;
    }

    public void ustawImieDlaId(int id, String imie) {
        if (id >= 1 && id <= 2) {
            imionaGraczy[id] = imie;
        }
    }

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
