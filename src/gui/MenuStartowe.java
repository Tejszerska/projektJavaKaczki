package gui;

import gra.Sprites;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class MenuStartowe extends JPanel {
    private OknoGry okno;
    private JTextField poleImie;

    public MenuStartowe(OknoGry okno) {
        this.okno = okno;

        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        // Tytuł gry
        JLabel tytul = new JLabel("Kaczuchy", JLabel.CENTER);
        tytul.setFont(new Font("Arial", Font.BOLD, 36));
        tytul.setBounds(250, 80, 300, 50);
        tytul.setForeground(Color.WHITE);
        add(tytul);

        // Etykieta "Podaj imię gracza"
        JLabel etykietaImie = new JLabel("Podaj imię gracza:");
        etykietaImie.setBounds(300, 150, 200, 40);
        etykietaImie.setForeground(Color.WHITE);
        etykietaImie.setFont(new Font("Arial", Font.ITALIC, 20));
        add(etykietaImie);

        // Pole do wpisania imienia
        poleImie = new JTextField();
        poleImie.setBounds(300, 180, 200, 40);
        add(poleImie);

        // Przycisk startu gry
        JButton start = new JButton("Start Gry");
        start.setFont(new Font("Arial", Font.BOLD, 24));
        start.setBounds(300, 230, 200, 50);
        start.addActionListener(e -> {
            String imie = poleImie.getText().trim();
            if (imie.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Wprowadź imię gracza.");
                return;
            }

            // Zapisz imię gracza i rozpocznij grę
            okno.ustawImieGracza(imie);
            okno.powiadomSerwerOStart();
            okno.rozpocznijNowaGre();
        });
        add(start);

        // Przycisk do wyświetlenia wyników z pliku CSV
        JButton wyniki = new JButton("Pokaż wyniki");
        wyniki.setFont(new Font("Arial", Font.PLAIN, 20));
        wyniki.setBounds(300, 300, 200, 40);
        wyniki.addActionListener(e -> {
            try {
                Path sciezka = Path.of("wyniki.csv");

                if (!Files.exists(sciezka)) {
                    JOptionPane.showMessageDialog(this, "Brak zapisanych wyników.");
                    return;
                }

                List<String> linie = Files.readAllLines(sciezka);

                if (linie.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Plik wyników jest pusty.");
                    return;
                }

                StringBuilder sb = new StringBuilder("Historia wyników:\n\n");

                // Odczyt danych i formatowanie
                for (String linia : linie) {
                    String[] dane = linia.split(",");
                    if (dane.length >= 4) {
                        String imieG = dane[0];
                        String trafienia = dane[1];
                        String data = dane[2];
                        String srednia = dane[3];
                        sb.append("Gracz: ").append(imieG).append("\n");
                        sb.append("Trafione kaczki: ").append(trafienia).append("\n");
                        sb.append("Data: ").append(data).append("\n");
                        sb.append("Śr. czas/kaczkę: ").append(srednia).append(" s\n");
                        sb.append("-------------------------\n");
                    }
                }

                JOptionPane.showMessageDialog(this, sb.toString(), "Wyniki", JOptionPane.INFORMATION_MESSAGE);

            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this, "Błąd przy czytaniu wyników.");
            }
        });
        add(wyniki);

        // Przycisk zakończenia gry
        JButton zakoncz = new JButton("Zakończ");
        zakoncz.setFont(new Font("Arial", Font.PLAIN, 20));
        zakoncz.setBounds(300, 350, 200, 40);
        zakoncz.addActionListener(e -> System.exit(0));
        add(zakoncz);
    }

    // Tło panelu
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo, 0, 0, getWidth(), getHeight(), null);
    }
}
