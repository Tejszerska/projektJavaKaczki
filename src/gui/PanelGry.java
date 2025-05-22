package gui;

import gra.Sprites;
import siec.DaneKaczki;
import siec.KlientGry;
import siec.StanGry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class PanelGry extends JPanel implements MouseMotionListener {
    private List<DaneKaczki> kaczki = new ArrayList<>();
    private int szerokosc = 800;
    private int wysokosc = 600;
    private int wynik1 = 0;
    private int wynik2 = 0;
    private KlientGry klient;

    private int mouseX = 0;
    private int mouseY = 0;

    public PanelGry(KlientGry klient) {
        this.klient = klient;
        setPreferredSize(new Dimension(szerokosc, wysokosc));
        setBackground(Color.CYAN);
        addMouseMotionListener(this);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (klient != null) {
                    klient.wyslijStrzal(e.getX(), e.getY());
                }
            }
        });

        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
        setCursor(blankCursor);
    }

    public void aktualizujStan(StanGry stan) {
        this.kaczki = stan.kaczki;
        this.wynik1 = stan.wynikGracza1;
        this.wynik2 = stan.wynikGracza2;

        if (stan.graZakonczona) {
            // zablokuj kliknięcia
            this.removeMouseListener(this.getMouseListeners()[0]);

            // pokaz panel koncowy (jeśli chcesz)
            JOptionPane.showMessageDialog(this, "Gra zakończona!");
            // lub:
            // okno.pokazPanel("koniec"); // jeżeli masz referencję do okna
        }

        repaint();
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo, 0, 0, getWidth(), getHeight(), null);

        for (DaneKaczki k : kaczki) {
            Image img = switch (k.grafika) {
                case "leci1" -> Sprites.leci1;
                case "leci2" -> Sprites.leci2;
                case "spada" -> Sprites.spada;
                default -> null;
            };
            if (img != null) {
                g.drawImage(img, k.x, k.y, null);
            }
        }

        g.setColor(Color.BLACK);
        g.drawString("Gracz 1: " + wynik1 + "   Gracz 2: " + wynik2, 10, 20);

        g.drawImage(Sprites.celownik, mouseX - 64, mouseY - 64, null);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    public void setKlient(KlientGry klient) {
        this.klient = klient;
    }
}
