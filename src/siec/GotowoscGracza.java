package siec;

import java.io.Serializable;

public class GotowoscGracza implements Serializable {

    public int graczId; // ID gracza, który zgłasza gotowość

    public GotowoscGracza(int graczId) {
        this.graczId = graczId;
    }
}
