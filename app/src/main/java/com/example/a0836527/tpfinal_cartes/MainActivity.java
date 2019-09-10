package com.example.a0836527.tpfinal_cartes;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;
import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    DatabaseHelper dh;

    Ecouteur ec;

    LinearLayout llPileCroissante1;
    LinearLayout llPileCroissante2;
    LinearLayout llPileDecroissante1;
    LinearLayout llPileDecroissante2;
    LinearLayout llLigneCartes1;
    LinearLayout llLigneCartes2;

    TextView tvPileCroissante1;
    TextView tvPileCroissante2;
    TextView tvPileDecroissante1;
    TextView tvPileDecroissante2;

    int textViewWidth;
    int textViewHeight;

    TextView tvNbCartesRestantes;
    int nbCartesRestantes;

    Button btnMenu;
    TextView tvChrono;
    Timer timer;
    TimerTask timerTask;
    int minutes;
    int secondes;

    TextView tvScore;
    int score;

    int valeurCarte1Croissante;
    int valeurCarte2Croissante;
    int valeurCarte1Decroissante;
    int valeurCarte2Decroissante;

    Intent versMenuPrincipal;
    Cartes cartes;
    Vector<Carte> paquetCartes;
    Regles regles;
    Vector<LinearLayout> pilesAPiger;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dh = DatabaseHelper.getInstance(this);

        btnMenu = findViewById(R.id.btnMenu);
        versMenuPrincipal = new Intent(MainActivity.this, MenuDepartActivity.class);
        tvChrono = findViewById(R.id.tvChrono);

        llPileCroissante1 = findViewById(R.id.llPileCroissante1);
        llPileCroissante2 = findViewById(R.id.llPileCroissante2);
        llPileDecroissante1 = findViewById(R.id.llPileDecroissante1);
        llPileDecroissante2 = findViewById(R.id.llPileDecroissante2);
        llLigneCartes1 = findViewById(R.id.llLigneCartes1);
        llLigneCartes2 = findViewById(R.id.llLigneCartes2);

        tvPileCroissante1 = findViewById(R.id.tvPileCroissante1);
        tvPileCroissante2 = findViewById(R.id.tvPileCroissante2);
        tvPileDecroissante1 = findViewById(R.id.tvPileDecroissante1);
        tvPileDecroissante2 = findViewById(R.id.tvPileDecroissante2);

        textViewWidth = (int)getResources().getDimension(R.dimen.tv_width);
        textViewHeight = (int)getResources().getDimension(R.dimen.tv_height);

        tvNbCartesRestantes = findViewById(R.id.tvNbCartesRestantes);
        nbCartesRestantes = 97;
        tvScore = findViewById(R.id.tvScore);
        score = 0;

        valeurCarte1Croissante = 0;
        valeurCarte2Croissante = 0;
        valeurCarte1Decroissante = 98;
        valeurCarte2Decroissante = 98;


        cartes = new Cartes();
        paquetCartes = cartes.getCartes();
        regles = new Regles();
        pilesAPiger = new Vector<LinearLayout>();

        pigerNouvellesCartes(llLigneCartes1);
        pigerNouvellesCartes(llLigneCartes2);


        ec = new Ecouteur();

        llPileCroissante1.setOnDragListener(ec);
        llPileCroissante2.setOnDragListener(ec);
        llPileDecroissante1.setOnDragListener(ec);
        llPileDecroissante2.setOnDragListener(ec);

        boucleAjouterEcouteur(llLigneCartes1);
        boucleAjouterEcouteur(llLigneCartes2);

        btnMenu.setOnClickListener(ec);

        minutes = 0;
        secondes = 0;
        timer = new Timer();
        timerTask = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    public void run() {

                    secondes++;

                    if(secondes == 60){
                        minutes++;
                        secondes = 0;
                    }

                    String secondesStr;

                    if(secondes <= 9){
                        secondesStr = "0" + Integer.toString(secondes);
                    }
                    else{
                        secondesStr = Integer.toString(secondes);
                    }

                    tvChrono.setText(minutes + ":" + secondesStr);

                }
            });
            }
        };
        timer.scheduleAtFixedRate(timerTask, 0, 1000);

    }

    private class Ecouteur implements View.OnDragListener, View.OnTouchListener, View.OnClickListener{

        @Override
        public boolean onDrag(View v, DragEvent event) {
            TextView carteDeplacee = (TextView) event.getLocalState();

            switch (event.getAction()){
                case DragEvent.ACTION_DRAG_ENTERED:
                    v.setBackgroundColor(Color.rgb(145, 187, 255));
                    break;

                case DragEvent.ACTION_DRAG_EXITED:
                    v.setBackgroundColor(Color.WHITE);
                    break;

                case DragEvent.ACTION_DROP:
                    LinearLayout containerCarteArrivee = (LinearLayout) v;

                    if(containerCarteArrivee == llPileCroissante1 || containerCarteArrivee == llPileCroissante2 || containerCarteArrivee == llPileDecroissante1 || containerCarteArrivee == llPileDecroissante2) {
                        TextView carteArrivee = (TextView) containerCarteArrivee.getChildAt(0);
                        LinearLayout containerCarteDeplacee = (LinearLayout) carteDeplacee.getParent();

                        int numCarteDeplacee = Integer.parseInt(carteDeplacee.getText().toString());
                        int numCarteArrivee = Integer.parseInt(carteArrivee.getText().toString());
                        String ordre = "";

                        if(containerCarteArrivee == llPileCroissante1 || containerCarteArrivee == llPileCroissante2){
                            ordre = "croissant";
                        }
                        else if(containerCarteArrivee == llPileDecroissante1 || containerCarteArrivee == llPileDecroissante2){
                            ordre = "decroissant";
                        }

                        if(regles.ordreNumeriqueRespecte(numCarteDeplacee, numCarteArrivee, ordre)) {
                            containerCarteDeplacee.removeView(carteDeplacee);
                            placerCarteDeplacee(carteDeplacee, carteArrivee);
                            pilesAPiger.add(containerCarteDeplacee);

                            if (pilesAPiger.size() == 2) {
                                for(LinearLayout pile : pilesAPiger){
                                    creerTextViewPourCarte(pile);
                                }
                                pilesAPiger.clear();
                            }

                            nbCartesRestantes --;
                            tvNbCartesRestantes.setText(nbCartesRestantes + " cartes");

                            int proximite = Math.abs(numCarteArrivee - numCarteDeplacee);
                            ajusterScore(proximite);

                            if(containerCarteArrivee == llPileCroissante1){
                                valeurCarte1Croissante = numCarteDeplacee;
                            }
                            else if(containerCarteArrivee == llPileCroissante2){
                                valeurCarte2Croissante = numCarteDeplacee;
                            }
                            else if(containerCarteArrivee == llPileDecroissante1){
                                valeurCarte1Decroissante = numCarteDeplacee;
                            }
                            else if(containerCarteArrivee == llPileDecroissante2){
                                valeurCarte2Decroissante = numCarteDeplacee;
                            }

//                            if(!verifierSiPossibleDeContinuer()){
//                                System.out.println("ici");
//                            }
                        }
                    }

                    break;

                case DragEvent.ACTION_DRAG_ENDED:
                    carteDeplacee.setVisibility(View.VISIBLE);
                    v.setBackgroundColor(Color.WHITE);
                    break;

                default:
                    break;
            }

            return true;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
            v.startDrag(null, shadowBuilder, v, 0);
            v.setVisibility(View.INVISIBLE);
            return true;
        }

        @Override
        public void onClick(View v) {
            if(score > dh.fetchHighScore()){
                dh.insertScore(score, dh.database);
            }

            startActivity(versMenuPrincipal);
        }
    }


    private void boucleAjouterEcouteur(LinearLayout linearLayout){
        int childcount = linearLayout.getChildCount();
        for (int i = 0; i < childcount; i++) {
            LinearLayout ll = (LinearLayout) linearLayout.getChildAt(i);
            ll.setOnDragListener(ec);
            TextView tv = (TextView) ll.getChildAt(0);
            tv.setOnTouchListener(ec);
        }
    }

    private void pigerNouvellesCartes(LinearLayout rangee){
        for(int i = 0; i < rangee.getChildCount(); i++){
            LinearLayout pile = (LinearLayout) rangee.getChildAt(i);
            TextView textView = (TextView) pile.getChildAt(0);
            pigerNouvelleCarte(textView);
        }
    }

    private void creerTextViewPourCarte(LinearLayout pile){
        TextView nouvelleCarte = new TextView(this);
        nouvelleCarte.setLayoutParams((new ViewGroup.LayoutParams(textViewWidth, textViewHeight)));
        nouvelleCarte.setBackgroundColor(Color.parseColor("#f28bbc"));
        nouvelleCarte.setGravity(Gravity.CENTER);
        pigerNouvelleCarte(nouvelleCarte);
        nouvelleCarte.setOnTouchListener(ec);
        pile.addView(nouvelleCarte);
    }

    private void pigerNouvelleCarte(TextView carte){
        String numeroCarte =  Integer.toString(paquetCartes.remove(0).getNumero());
        carte.setText(numeroCarte);
    }

    private void placerCarteDeplacee(TextView carteDeplacee, TextView carteArrivee){
        String numeroCarteDeplacee = carteDeplacee.getText().toString();
        carteArrivee.setText(numeroCarteDeplacee);
    }

    private void ajusterScore(int proximite){
        //Ajout de base pur une carte placee
        int scoreAAjouter = 100;

        //ajuste selon le temps (en bas de une minute ou en bas de 2 minutes)
        if(minutes == 0){
            scoreAAjouter *= 2;
        }
        else if(minutes == 1){
            scoreAAjouter *= 1.5;
        }

        //ajuste selon la proximite de la valeur des cartes
        scoreAAjouter += (100 - ((proximite - (proximite % 10))));

        //ajuste selon le nombre de cartes restante a placer
        scoreAAjouter *= 10 - ((nbCartesRestantes -  (nbCartesRestantes % 10)) / 10);

        score += scoreAAjouter;
        String scoreStr = Integer.toString(score);
        tvScore.setText(scoreStr);
    }

    public boolean verifierSiPossibleDeContinuer(){
        boolean possible = false;

        int childcount = llLigneCartes1.getChildCount();
        for (int i = 0; i < childcount; i++) {
            LinearLayout ll = (LinearLayout) llLigneCartes1.getChildAt(i);
            TextView tv = (TextView) ll.getChildAt(0);
            int numCarte = Integer.parseInt(tv.getText().toString());

            if(regles.ordreNumeriqueRespecte(numCarte, valeurCarte1Croissante, "croissant") || regles.ordreNumeriqueRespecte(numCarte, valeurCarte2Croissante, "croissant") || regles.ordreNumeriqueRespecte(numCarte, valeurCarte1Decroissante, "decroissant") || regles.ordreNumeriqueRespecte(numCarte, valeurCarte2Decroissante, "decroissant")){
                possible = true;
                break;
            }
        }

        return possible;
    }

//    @Override
//    protected void onStop() {
//        dh.fermerDB();
//        super.onStop();
//    }
}
