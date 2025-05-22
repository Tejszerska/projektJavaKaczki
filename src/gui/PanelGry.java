package gui;

import gra.Sprites;
import siec.DaneKaczki;
import siec.KlientGry;
import siec.StanGry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
    private OknoGry okno;
    private long czasStartu = 0;
    private boolean graZakonczona = false;
    private final MouseListener listener = new MouseAdapter() {
        @Override
        public void mousePressed(MouseEvent e) {
            if (klient != null) {
                klient.wyslijStrzal(e.getX(), e.getY());
            }
        }
    };


    public PanelGry(KlientGry klient, OknoGry okno) {
        this.klient = klient;
        this.okno = okno;
        this.addMouseListener(listener);

        setPreferredSize(new Dimension(szerokosc, wysokosc));
        setBackground(Color.CYAN);
        addMouseMotionListener(this);

        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank");
        setCursor(blankCursor);
    }

    public void aktualizujStan(StanGry stan) {
        this.kaczki = stan.kaczki;
        this.wynik1 = stan.wynikGracza1;
        this.wynik2 = stan.wynikGracza2;
        repaint();

        if (stan.graZakonczona && !graZakonczona) {
            graZakonczona = true;
            long czasZakonczenia = System.currentTimeMillis();
            double czasSekundy = (czasZakonczenia - czasStartu) / 1000.0;

            // Zapis do pliku CSV
            zapiszWynikDoPliku(okno.pobierzImieGracza(), wynik1, czasSekundy);

            // Panel końcowy
            okno.pobierzPanelKoniec().ustawStatystykiGraczy(
                    stan.wynikGracza1,
                    stan.wynikGracza2,
                    czasSekundy
            );
            okno.pokazPanel("koniec");

            for (MouseListener l : getMouseListeners()) removeMouseListener(l);
        }


    }
    private void zapiszWynikDoPliku(String imie, int trafienia, double czasSekundy) {
        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date());
        double srednia = trafienia > 0 ? czasSekundy / trafienia : czasSekundy;

        // Format: imię,trafienia,data,średni czas
        String linia = String.format("%s,%d,%s,%.2f\n", imie, trafienia, data, srednia);

        try (FileWriter writer = new FileWriter("wyniki.csv", true)) {
            writer.write(linia);
        } catch (IOException e) {
            e.printStackTrace();
        }
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

    public void rozpocznijNowaGre() {
        if (getMouseListeners().length == 0) {
            addMouseListener(listener);
        }
        this.czasStartu = System.currentTimeMillis();  // ← DODANE
        this.graZakonczona = false;
    }



    public KlientGry getKlient() {
        return klient;
    }

}
