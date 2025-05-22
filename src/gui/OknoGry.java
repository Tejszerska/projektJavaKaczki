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

    private String imieGracza1 = "";
    private String imieGracza2 = "";
    private int licznikImion = 0;
    public static String imieGraczaStatic = "";



    public OknoGry() throws IOException {
        setTitle("Kaczuchy Multiplayer");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        MenuStartowe menuStartowe = new MenuStartowe(this);
        panelKoniec = new PanelKoniec(this);

        // Utwórz PanelGry i klienta, przekaż klient do PanelGry
        final PanelGry[] tempPanel = new PanelGry[1];

        KlientGry klient = new KlientGry("localhost", 5555, stan -> {
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
        imieGraczaStatic = imie;
    }


    public String getImieGracza1() {
        return imieGracza1;
    }

    public String getImieGracza2() {
        return imieGracza2;
    }

    public String pobierzImieGracza() {
        return imieGracza;
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
