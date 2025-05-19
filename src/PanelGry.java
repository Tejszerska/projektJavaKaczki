import javax.swing.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseAdapter;
import java.util.ArrayList;

public class PanelGry extends JPanel implements MouseMotionListener
{
    private int mouseX = 0, mouseY = 0;
    private List<Kaczka> kaczki = new ArrayList<>();
    private int wynik = 0;
    private Celownik celownik;

    public PanelGry()
    {
        setPreferredSize(new Dimension(800, 600));
        setBackground(Color.CYAN);
        addMouseMotionListener(this);

        for (int i = 0; i < 5; i++) // dodanie kaczek na tą chwile 5
        {
            kaczki.add(new Kaczka());
        }

        celownik = new Celownik(0,0); // tworzymy celownik i podajemy pola do konstruktora

        //timer aktualizujacy stan gry co 16ms
        Timer timer = new Timer(16, e ->
        {
                Iterator<Kaczka> it = kaczki.iterator();
                while (it.hasNext())
                {
                    Kaczka k = it.next();
                    k.move();

                    // usuwanie kaczek, które spadły z ekranu
                    if (k.getY() > 600) {
                        it.remove();
                    }
                }
            repaint();
        });
        timer.start();

        addMouseListener(new MouseAdapter() // wlaczenie listenera klikniec myszy
        {
            @Override
            public void mousePressed(MouseEvent e) //wywolywanie gdy klikniemy w obrebie PanelGry(bez czekania na puszczenie przycisku)
            {
               for (Kaczka k : kaczki)
               {
                   if (k.trafienie(e.getX(), e.getY()))
                   {
                       wynik++;
                       break;
                   }
               }
            }
        });

        //usuniecie domyslnego kursora z pola gry
        BufferedImage cursor = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);
        Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(cursor , new Point(0,0), "blank cursor");
        setCursor(blankCursor);
    }

    @Override
    protected void paintComponent(Graphics g) // kolorowanie kaczek i celownika
    {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo,0,0,getWidth(),getHeight(),null);
        for (Kaczka k : kaczki)
        {
            k.draw(g);
        }

        g.setColor(Color.BLACK);
        g.drawString("Wynik: " + wynik, 10, 20);

        celownik.draw(g);
    }

    @Override
    public void mouseMoved(MouseEvent e) // lokalizacja pozycji myszy
    {
        mouseX = e.getX();
        mouseY = e.getY();
        celownik.setPosition(e.getX(), e.getY()); //wywolujemy metode z klasy celownik dla ustalenia jego pozycji
    }

    @Override//niepotrzebne ale trzeba nadpisać z powodu implementacji interfejsu
    public void mouseDragged(MouseEvent e)
    {
    }
}
