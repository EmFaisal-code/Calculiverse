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

public class RuangActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_ruang);

        CardView cardKubus = findViewById(R.id.card_kubus);
        CardView cardPrisma = findViewById(R.id.card_prisma);
        CardView cardTabung = findViewById(R.id.card_tabung);
        CardView cardBola = findViewById(R.id.card_bola);

//        cardKubus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RuangActivity.this, KubusActivity.class));
//            }
//        });
        cardPrisma.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RuangActivity.this, PrismaActivity.class));
            }
        });
//        cardTabung.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RuangActivity.this, TabungActivity.class));
//            }
//        });
//        cardBola.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(RuangActivity.this, BolaActivity.class));
//            }
//        });
    }
}