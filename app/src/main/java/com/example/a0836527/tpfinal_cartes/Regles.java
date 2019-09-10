package com.example.a0836527.tpfinal_cartes;

public class Regles {

    public Regles(){};

    public boolean ordreNumeriqueRespecte(int nombreDepart, int nombreArrivee, String ordre){
        boolean valide = false;

        if(ordre.equals("croissant")){
            if(nombreDepart > nombreArrivee || nombreDepart == (nombreArrivee - 10)){
                valide = true;
            }
        }
        else if(ordre.equals("decroissant")){
            if(nombreDepart < nombreArrivee || nombreDepart == (nombreArrivee + 10)){
                valide = true;
            }
        }

        return valide;
    }


}
