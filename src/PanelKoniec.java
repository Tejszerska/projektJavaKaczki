import javax.swing.*;
import java.awt.*;

public class PanelKoniec extends JPanel {
    private OknoGry okno;

    public PanelKoniec(OknoGry okno) {
        this.okno = okno;
        setPreferredSize(new Dimension(800, 600));
        setLayout(null);

        JLabel koniec = new JLabel("Koniec Gry!");
        koniec.setFont(new Font("Arial", Font.BOLD, 36));
        koniec.setForeground(Color.WHITE);
        koniec.setBounds(250, 100, 300, 50);
        koniec.setHorizontalAlignment(SwingConstants.CENTER);
        add(koniec);

        JButton zagrajPonownie = new JButton("Zagraj ponownie");
        zagrajPonownie.setFont(new Font("Arial", Font.PLAIN, 24));
        zagrajPonownie.setBounds(275, 200, 250, 50);
        zagrajPonownie.addActionListener(e -> okno.rozpocznijNowaGre());
        add(zagrajPonownie);

        JButton powrotDoMenu = new JButton("Powrót do menu");
        powrotDoMenu.setFont(new Font("Arial", Font.PLAIN, 20));
        powrotDoMenu.setBounds(275, 300, 250, 40);
        powrotDoMenu.addActionListener(e -> okno.pokazPanel("menu"));
        add(powrotDoMenu);

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Sprites.tlo, 0, 0, getWidth(), getHeight(), null);
    }
}
