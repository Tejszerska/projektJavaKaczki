package gui;

import gra.Celownik;
import gra.Kaczka;
import gra.Sprites;
import gui.OknoGry;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

public class PanelGry extends JPanel implements MouseMotionListener {
    private int mouseX = 0, mouseY = 0;
    private List<Kaczka> kaczki = new ArrayList<>();
    private int postep = 0;
    private Celownik celownik;
    private OknoGry okno;
    private int maxPostep = 5;
    private long startCzas;
    private long koniecCzas;

    public PanelGry(OknoGry okno) {
        this.okno = okno;
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);
        addMouseMotionListener(this);

        celownik = new Celownik(0, 0);

        Timer timer = new Timer(16, e -> {
            Iterator<Kaczka> it = kaczki.iterator();
            while (it.hasNext()) {
                Kaczka k = it.next();
                k.move();
                if (k.getY() > 600) {
                    it.remove();
                }
            }
            repaint();
        });
        timer.start();

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                for (Kaczka k : kaczki) {
                    if (k.trafienie(e.getX(), e.getY())) {
                        postep++;
                        if (postep >= maxPostep) {
                            koniecCzas = System.currentTimeMillis();
                            long czasGry = koniecCzas - startCzas;
                            double czasSekundy = czasGry / 1000.0;
                            double srednia = czasSekundy / maxPostep;

                            okno.pobierzPanelKoniec().ustawStatystyki(maxPostep, czasSekundy, srednia);
                            zapiszWynik(maxPostep, srednia);
                            okno.pokazPanel("koniec");
                        }
                        break;
                    }
                }
            }
        });

        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor, new Point(0, 0), "blank cursor");
        setCursor(blankCursor);
    }

    private void zapiszWynik(int liczbaKaczek, double sredniaCzasNaKaczke) {
        String imie = okno.pobierzImieGracza();
        String data = java.time.LocalDateTime.now()
                .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
        String linia = imie + "," + liczbaKaczek + "," + data + "," + String.format(Locale.US, "%.2f", sredniaCzasNaKaczke); // Locale.US wymusza użycie kropki, bez tego źle odczytuje wyniki z pliku csv (niejawne parsowanie double)
        try (PrintWriter pw = new PrintWriter(new FileWriter("wyniki.csv", true))) {
            pw.println(linia);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void reset() {
        postep = 0;
        kaczki.clear();
        for (int i = 0; i < 5; i++) {
            kaczki.add(new Kaczka());
        }
        startCzas = System.currentTimeMillis();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo, 0, 0, getWidth(), getHeight(), null);
        for (Kaczka k : kaczki) {
            k.draw(g);
        }
        g.setColor(Color.BLACK);
        g.drawString("Postęp: " + postep + " / " + maxPostep, 10, 20);
        celownik.draw(g);
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mouseX = e.getX();
        mouseY = e.getY();
        celownik.setPosition(mouseX, mouseY);
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }
}
