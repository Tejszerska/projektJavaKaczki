package gra;

import java.awt.*;

public class Celownik extends GraObject
{
    public Celownik(int x, int y) //konstruktor dziedziczący z gra.GraObject
    {
        super(x,y,128,128);
    }

    @Override
    public void move() // nadpisanie metody abstrakcyjnej
    {
    }

    @Override
    public void draw(Graphics g) // rysujemy krzyzyk
    {
       // g.setColor(Color.RED);
       // g.drawLine(x, y + h / 2, x + w, y + h / 2);
       // g.drawLine(x + w / 2,y, x + w / 2, y + h);
        g.drawImage(Sprites.celownik,x,y, null);
    }

    public void setPosition(int mouseX,int mouseY) // zczytujemy dokladna pozycje celownika a nie lewy gorny rog
    {
        this.x = mouseX - w / 2;
        this.y = mouseY - h / 2;
    }
}
