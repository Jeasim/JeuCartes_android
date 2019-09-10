package com.example.a0836527.tpfinal_cartes;

import java.util.Collections;
import java.util.Vector;

public class Cartes {

    Vector<Carte> cartes;

    public Cartes(){
        cartes = new Vector<Carte>();
        for(int i = 1; i < 98; i++){
            cartes.add(new Carte(i));
        }
        Collections.shuffle(cartes);
    }

    public Vector<Carte> getCartes(){
        return cartes;
    }

}
