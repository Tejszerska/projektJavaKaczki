package gui;

import gra.Sprites;
import gui.OknoGry;

import javax.swing.*;
import java.awt.*;

public class PanelKoniec extends JPanel {
    private OknoGry okno;
    private JLabel wynikLabel;
    private JLabel czasLabel;
    private JLabel sredniaLabel;

    public PanelKoniec(OknoGry okno) {
        this.okno = okno;
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        JLabel koniec = new JLabel("Koniec Gry!", JLabel.CENTER);
        koniec.setFont(new Font("Arial", Font.BOLD, 36));
        koniec.setForeground(Color.WHITE);
        koniec.setBounds(0, 80, 800, 50); // wyśrodkowany na całą szerokość
        add(koniec);

        wynikLabel = new JLabel("", JLabel.CENTER);
        wynikLabel.setFont(new Font("Arial", Font.PLAIN, 24));
        wynikLabel.setForeground(Color.WHITE);
        wynikLabel.setBounds(0, 160, 800, 30);
        add(wynikLabel);

        czasLabel = new JLabel("", JLabel.CENTER);
        czasLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        czasLabel.setForeground(Color.WHITE);
        czasLabel.setBounds(0, 200, 800, 25);
        add(czasLabel);

        sredniaLabel = new JLabel("", JLabel.CENTER);
        sredniaLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        sredniaLabel.setForeground(Color.WHITE);
        sredniaLabel.setBounds(0, 230, 800, 25);
        add(sredniaLabel);

        JButton zagrajPonownie = new JButton("Zagraj ponownie");
        zagrajPonownie.setFont(new Font("Arial", Font.PLAIN, 20));
        zagrajPonownie.setBounds(275, 280, 250, 50);
        zagrajPonownie.addActionListener(e -> {
            okno.powiadomSerwerOStart(); // <<< bez tego serwer nie ruszy
            okno.rozpocznijNowaGre();
            wynikLabel.setText("");
            czasLabel.setText("");
            sredniaLabel.setText("");
        });

        add(zagrajPonownie);

        JButton powrotDoMenu = new JButton("Powrót do menu");
        powrotDoMenu.setFont(new Font("Arial", Font.PLAIN, 20));
        powrotDoMenu.setBounds(275, 340, 250, 40);
        powrotDoMenu.addActionListener(e -> {
            okno.pokazPanel("menu");
            wynikLabel.setText("");
            czasLabel.setText("");
            sredniaLabel.setText("");
        });
        add(powrotDoMenu);
    }
    public void ustawStatystykiGraczy(String imie1, String imie2, int wynikA, int wynikB, double czasSekundy) {
        String zwyciezca;
        if (wynikA > wynikB) zwyciezca = imie1 + " wygrał!";
        else if (wynikB > wynikA) zwyciezca = imie2 + " wygrał!";
        else zwyciezca = "Remis!";
        wynikLabel.setText(imie1 + ": " + wynikA + " | " + imie2 + ": " + wynikB);
        czasLabel.setText("Czas gry: " + String.format("%.2f", czasSekundy) + " s");
        sredniaLabel.setText(zwyciezca);
    }



    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo, 0, 0, getWidth(), getHeight(), null);
    }
}
