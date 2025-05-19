import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Random;

public class Kaczka extends GraObject
{
    private int dx, dy; //wektory prędkośći z jaką porusza sie kaczka
    private Color color; //kolor kaczki
    private BufferedImage currentImage;  //wczytanie grafik kaczek
    private boolean zestrzelona = false;
    private int licznikAnimacji = 0;

    private static final Random random = new Random();

    public Kaczka()
    {
        super(random.nextInt(750), random.nextInt(500), 128, 128);
        dx = random.nextInt(3) + 1; // losowanie predkości kaczki w osi x
        dy = random.nextInt(3) + 1; // losowanie predkosci kaczki w osi y
        color = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat()); // losowy kolor dla kaczki
        currentImage = Sprites.leci1; // ustawnienie startowej grafiki
    }

    @Override
    public void move()
    {
        if (!zestrzelona)
        {
            // LECĄCA KACZKA
            x += dx;
            y += dy;

            if (x <= 0 || x + w >= 800) dx = -dx;
            if (y <= 0 || y + h >= 600) dy = -dy;

            licznikAnimacji++;
            currentImage = (licznikAnimacji / 10) % 2 == 0 ? Sprites.leci1 : Sprites.leci2;

        } else {
            // ZESTRZELONA KACZKA
            y += 4; // tylko pionowo w dół
            currentImage = Sprites.spada;
        }
    }

    @Override
    public void draw(Graphics g) // rysuje kaczke jako kolorowy oval na danej pozycji
    {
        g.drawImage(currentImage, x, y, null);
    }

    public boolean trafienie(int mouseX, int mouseY) {
        if (!zestrzelona && trafiona(mouseX, mouseY))
        {
            zestrzelona = true;
            return true;
        }
        return false;
    }
}
