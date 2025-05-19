import javax.swing.*;
import java.awt.*;

public class OknoGry extends JFrame{
    private CardLayout cardLayout;
    private JPanel cards;
    public OknoGry()
    {
        setTitle("Kaczuchy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        cardLayout = new CardLayout();
        cards = new JPanel(cardLayout);

        MenuStartowe menuStartowe = new MenuStartowe(this);
        PanelGry panelGry = new PanelGry(this); // przekaż referencję do okna
        PanelKoniec panelKoniec = new PanelKoniec(this);

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
}
