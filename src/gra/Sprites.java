package gra;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Sprites
{
    public static BufferedImage spriteSheet;

    public static BufferedImage leci1;
    public static BufferedImage leci2;
    public static BufferedImage spada;
    public static BufferedImage celownik;
    public static BufferedImage tlo;

    public static void load() throws IOException
    {
 spriteSheet = ImageIO.read(Sprites.class.getResource("/sprites.png"));

 leci1 = spriteSheet.getSubimage(0,0,128,128);
 leci2 = spriteSheet.getSubimage(128,0,128,128);
 spada = spriteSheet.getSubimage(0,128,128,128);
 celownik = spriteSheet.getSubimage(128,128,128,128);

 tlo = ImageIO.read(Sprites.class.getResource("/tlo.png"));
    }
}
