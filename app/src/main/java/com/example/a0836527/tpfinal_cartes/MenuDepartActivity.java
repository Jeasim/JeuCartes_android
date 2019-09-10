package com.example.a0836527.tpfinal_cartes;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MenuDepartActivity extends AppCompatActivity {

    TextView tvMeilleurScore;
    Button btnLancerPartie;

    DatabaseHelper dh;
    Ecouteur ec;
    Intent versMainActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_depart);

        dh = DatabaseHelper.getInstance(this);

        tvMeilleurScore = findViewById(R.id.textViewMeilleurScore);
        btnLancerPartie = findViewById(R.id.btnDémarrerJeu);

        tvMeilleurScore.setText("Meilleur score: " + dh.fetchHighScore());

        ec = new Ecouteur();
        btnLancerPartie.setOnClickListener(ec);
    }


    private class Ecouteur implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            versMainActivity = new Intent(MenuDepartActivity.this, MainActivity.class);
            startActivity(versMainActivity);
        }
    }

//    @Override
//    protected void onStop() {
//        dh.fermerDB();
//        super.onStop();
//    }

}
