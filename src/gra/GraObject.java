package gra;

import java.awt.Graphics;

public abstract class GraObject
{
    protected int x, y; // wykorzystamy dla pozycji kaczki
    protected int w, h; // wykorzystamy dla rozmiaru kaczki

    public GraObject(int x, int y, int w, int h)
    {
 this.x = x;
 this.y = y;
 this.w = w;
 this.h = h;
    }

    public int getX()
    {
 return x;
    }

    public int getY()
    {
 return y;
    }

    public int getW()
    {
 return w;
    }

    public int getH()
    {
 return h;
    }

    public abstract void move();
    public abstract void draw(Graphics g);

    public boolean trafiona(int mouseX,int mouseY)
    {
 return mouseX >= x && mouseX <= x + w && mouseY >= y && mouseY <= y + h;
    }
}
