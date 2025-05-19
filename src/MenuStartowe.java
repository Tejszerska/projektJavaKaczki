import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MenuStartowe extends JPanel {
    private OknoGry okno;

    public MenuStartowe(OknoGry okno) {
        this.okno = okno;
        setPreferredSize(new Dimension(800, 600));
        setLayout(null); // pozwala ręcznie ustawiać pozycje elementów

        //start
        JButton start = new JButton("Start Gry");
        start.setFont(new Font("Arial", Font.BOLD, 24));
        start.setBounds(300, 200, 200, 50);
        start.addActionListener(e -> okno.rozpocznijNowaGre());
        add(start);

        // pokaz wyniki
        JButton wyniki = new JButton("Pokaż wyniki");
        wyniki.setFont(new Font("Arial", Font.PLAIN, 20));
        wyniki.setBounds(300, 270, 200, 40);
//        wyniki.addActionListener());
        add(wyniki);

        // zakoncz
        JButton zakoncz = new JButton("Zakończ");
        zakoncz.setFont(new Font("Arial", Font.PLAIN, 20));
        zakoncz.setBounds(300, 320, 200, 40);
        zakoncz.addActionListener(e -> System.exit(0));
        add(zakoncz);
    }

    // Rysowanie tła – identyczne jak w grze
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo, 0, 0, getWidth(), getHeight(), null);
    }
}
