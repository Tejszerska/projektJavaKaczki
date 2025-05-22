package main;

import gra.Sprites;
import gui.OknoGry;

import java.io.IOException;

public class Main
{
    public static void main(String[] args) throws IOException {
        try
        {
            Sprites.load();
        }
        catch (IOException e)
        {
            e.printStackTrace();
            return;
        }
        new OknoGry();
    }
}