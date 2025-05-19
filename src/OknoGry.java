import javax.swing.*;

public class OknoGry extends JFrame
{
    public OknoGry()
    {
        setTitle("Kaczuchy");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        setContentPane(new PanelGry());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }
}
