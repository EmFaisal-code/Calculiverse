package com.example.calculiverse;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.cardview.widget.CardView;

public class DatarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_datar);

        CardView cardSegitiga = findViewById(R.id.card_segitiga);
        CardView cardSegitigaSiku2 = findViewById(R.id.card_segitiga_siku2);
        CardView cardBujursangkar = findViewById(R.id.card_bujursangkar);
        CardView cardSegiempat = findViewById(R.id.card_segiempat);
        CardView cardLingkaran = findViewById(R.id.card_lingkaran);

        cardSegitiga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatarActivity.this, SegitigaActivity.class));
            }
        });
        cardSegitigaSiku2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatarActivity.this, SegitigaSiku2Activity.class));
            }
        });
        cardBujursangkar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatarActivity.this, BujurSangkarActivity.class));
            }
        });
        cardSegiempat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatarActivity.this, SegiEmpatActivity.class));
            }
        });
        cardLingkaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(DatarActivity.this, LingkaranActivity.class));
            }
        });
    }
}