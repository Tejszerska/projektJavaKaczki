package gui;

import javax.swing.*;
import java.awt.*;

public class OknoGry extends JFrame {
    private CardLayout cardLayout;
    private JPanel cards;

    private PanelGry panelGry;
    private PanelKoniec panelKoniec;
    private String imieGracza = "";

    public OknoGry() {
        setTitle("Kaczuchy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        panelGry = new PanelGry(this);
        MenuStartowe menuStartowe = new MenuStartowe(this);
        panelKoniec = new PanelKoniec(this);

        cards.add(menuStartowe, "menu");
        cards.add(panelGry, "gra");
        cards.add(panelKoniec, "koniec");

        setContentPane(cards);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void pokazPanel(String nazwaPanelu) {
        cardLayout.show(cards, nazwaPanelu);
    }

    public void rozpocznijNowaGre() {
        panelGry.reset();
        pokazPanel("gra");
    }

    public void ustawImieGracza(String imie) {
        this.imieGracza = imie;
    }

    public String pobierzImieGracza() {
        return imieGracza;
    }

    public PanelKoniec pobierzPanelKoniec() {
        return panelKoniec;
    }
}
