package com.example.calculiverse;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.room.Room;

public class PrismaActivity extends AppCompatActivity {
    private EditText etA, etB, etC;
    private TextView tvLuas, tvLPermukaan, tvVol;
    private ImageButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prisma);

        etA = findViewById(R.id.et_input_a);
        etB = findViewById(R.id.et_input_b);
        etC = findViewById(R.id.et_input_c);
        tvLuas = findViewById(R.id.tv_luas);
        tvLPermukaan = findViewById(R.id.tv_l_permukaan);
        tvVol = findViewById(R.id.tv_vol);
        clearButton = findViewById(R.id.clear_button);

        TextWatcher watcher = new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                hitungPrisma();
            }
        };
        etA.addTextChangedListener(watcher);
        etB.addTextChangedListener(watcher);
        etC.addTextChangedListener(watcher);

        clearButton.setOnClickListener(v -> {
            etA.setText("");
            etB.setText("");
            etC.setText("");
            tvLuas.setText("-");
            tvLPermukaan.setText("-");
            tvVol.setText("-");
        });
        ImageButton btnAddFavorite = findViewById(R.id.btn_add_favorite);
        AppDatabase db = Room.databaseBuilder(
                getApplicationContext(),
                AppDatabase.class, "favorit-db"
        ).allowMainThreadQueries().build();

        btnAddFavorite.setOnClickListener(v -> {
            boolean isFavorit = db.favoritDao().isFavorit("Prisma");
            if (isFavorit) {
                db.favoritDao().deleteByKategoriAndSubkategori("Ruang", "Prisma");
                Toast.makeText(this, "Dihapus dari Favorit", Toast.LENGTH_SHORT).show();
            } else {
                Favorit favorit = new Favorit("Ruang", "PrismaW");
                db.favoritDao().insert(favorit);
                Toast.makeText(this, "Ditambahkan ke Favorit", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void hitungPrisma() {
        String sa = etA.getText().toString();
        String sb = etB.getText().toString();
        String sc = etC.getText().toString();
        if (sa.isEmpty() || sb.isEmpty() || sc.isEmpty()) {
            tvLuas.setText("-");
            tvLPermukaan.setText("-");
            tvVol.setText("-");
            return;
        }
        try {
            double a = Double.parseDouble(sa);
            double b = Double.parseDouble(sb);
            double c = Double.parseDouble(sc);
            if (a <= 0 || b <= 0 || c <= 0) {
                tvLuas.setText("-");
                tvLPermukaan.setText("-");
                tvVol.setText("-");
                return;
            }
            // Prisma segiempat: luas alas = a*b, keliling alas = 2(a+b), luas permukaan = 2ab + 2c(a+b), volume = a*b*c
            double luasAlas = a * b;
            double kelilingAlas = 2 * (a + b);
            double luasPermukaan = 2 * luasAlas + kelilingAlas * c;
            double volume = a * b * c;
            tvLuas.setText(String.format("%.4f", luasAlas));
            tvLPermukaan.setText(String.format("%.4f", luasPermukaan));
            tvVol.setText(String.format("%.4f", volume));
        } catch (Exception e) {
            tvLuas.setText("-");
            tvLPermukaan.setText("-");
            tvVol.setText("-");
        }
    }
}